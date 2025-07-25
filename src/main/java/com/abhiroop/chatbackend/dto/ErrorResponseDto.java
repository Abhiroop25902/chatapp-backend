package com.abhiroop.chatbackend.dto;

import com.abhiroop.chatbackend.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record ErrorResponseDto(
        @NotBlank
        ErrorCode errorCode,
        @NotBlank
        Map<String, String> details
) {

}
