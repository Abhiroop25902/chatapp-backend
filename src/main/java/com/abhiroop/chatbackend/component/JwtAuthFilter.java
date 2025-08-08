package com.abhiroop.chatbackend.component;

import com.abhiroop.chatbackend.exception.JwtValidationException;
import com.abhiroop.chatbackend.service.JwtService;
import com.abhiroop.chatbackend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.abhiroop.chatbackend.lib.UnauthenticatedPaths.EXCLUDED_PATHS;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    static final String AUTHORIZATION_HEADER = "Authorization";
    static final String ERROR_MESSAGE = "Invalid or Expired JWT Token";
    static final String ACCOUNT_LOCKED = "Account locked";

    final JwtService jwtService;
    final UserService userService;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        super();
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (EXCLUDED_PATHS.stream().anyMatch(request.getRequestURI()::equals)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header not present in request or in wrong format; authHeader={}",
                    authHeader != null ? authHeader.substring(0, 10) : null
            );
            throw new JwtValidationException(ERROR_MESSAGE);
        }

        //NOTE: token will be like "Bearer xyz" -> actual token starts from index 7
        String jwt = authHeader.substring(7);

        // check if jwt is still valid
        if (jwtService.isTokenExpired(jwt)) {
            log.warn("JWT Token is expired; jwt={}", jwt.substring(0, 10));
            throw new JwtValidationException(ERROR_MESSAGE);
        }

        final var uuid = jwtService.getUuidFromToken(jwt);

        if (!userService.uuidPresent(uuid)) {
            log.warn("User associated with jwt is not found, jwt might be compromised, jwt={}", jwt.substring(0, 10));
            throw new JwtValidationException(ERROR_MESSAGE);
        }

        final var user = userService.getUserById(uuid);

        if (user.isAccountLocked() &&
                Optional.of(user.getLockTime()).map(time -> time.isAfter(LocalDateTime.now())).orElse(false)
        ) {
            log.warn("User associated with jwt is found, but the account is locked, jwt={}", jwt.substring(0, 10));
            throw new JwtValidationException(ACCOUNT_LOCKED);
        }

        userService.unlockUser(user);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            final var token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
