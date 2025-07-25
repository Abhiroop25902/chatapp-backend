package com.abhiroop.chatbackend.exception;

import com.abhiroop.chatbackend.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_REASON = "reason";
    private static final String AN_INTERNAL_ERROR_OCCURRED_PLEASE_CONTACT_SUPPORT = "An internal error occurred. Please contact support.";
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationError(MethodArgumentNotValidException ex) {
        log.warn(ErrorCode.VALIDATION_FAILED.toString(), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponseDto(
                ErrorCode.VALIDATION_FAILED,
                errors
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolations(ConstraintViolationException ex) {
        log.warn(ErrorCode.CONSTRAINT_VALIDATION_FAILED.toString(), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );
        return ResponseEntity.badRequest().body(new ErrorResponseDto(
                ErrorCode.CONSTRAINT_VALIDATION_FAILED,
                errors
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn(ErrorCode.DATA_INTEGRITY_VALIDATION_FAILED.toString(), ex);

        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR_REASON, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(
                ErrorCode.DATA_INTEGRITY_VALIDATION_FAILED,
                errors
        ));
    }

    @ExceptionHandler(EmailAlreadyPresentException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyPresent(EmailAlreadyPresentException ex) {
        log.warn(ErrorCode.EMAIL_ALREADY_PRESENT.toString(), ex);

        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR_REASON, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(
                ErrorCode.EMAIL_ALREADY_PRESENT, errors
        ));
    }

    @ExceptionHandler(PasswordValidationFailException.class)
    public ResponseEntity<ErrorResponseDto> handleFailedPasswordException(PasswordValidationFailException ex) {
        log.warn(ErrorCode.PASSWORD_VALIDATION_FAILED.toString(), ex);

        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR_REASON, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(
                        ErrorCode.PASSWORD_VALIDATION_FAILED,
                        errors
                )
        );
    }

    @ExceptionHandler(UserNameGenerationFailureException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNameGenerationFailure(UserNameGenerationFailureException ex) {
        log.warn(ErrorCode.USERNAME_GENERATION_FAILED.toString(), ex);

        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR_REASON, ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(
                ErrorCode.USERNAME_GENERATION_FAILED, errors
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        log.error(ErrorCode.UNHANDLED_EXCEPTION_OCCURRED.toString(), ex);

        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR_REASON, AN_INTERNAL_ERROR_OCCURRED_PLEASE_CONTACT_SUPPORT);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDto(
                        ErrorCode.UNHANDLED_EXCEPTION_OCCURRED, errors
                )
        );
    }
}
