package com.abhiroop.chatbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.abhiroop.chatbackend.lib.Constants.SCHEMA_NAME;
import static com.abhiroop.chatbackend.lib.Constants.USER_TABLE_NAME;

@Setter
@Getter
@Entity
@Table(name = USER_TABLE_NAME, schema = SCHEMA_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "created_at", updatable = false)
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "updated_at")
    @UpdateTimestamp
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(nullable = false, name = "failed_attempts")
    @Builder.Default
    private int failedAttempts = 0;

    @Column(nullable = false, name = "account_locked")
    @Builder.Default
    private boolean accountLocked = false;

    @Column(name = "lock_time")
    @Builder.Default
    private LocalDateTime lockTime = null;

    @Column(name = "roles", nullable = false)
    @Builder.Default
    private String roles = "ROLE_USER";

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;


    public void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public void unlockAccountForLogin() {
        this.lockTime = null;
        this.accountLocked = false;
        this.failedAttempts = 0;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

}
