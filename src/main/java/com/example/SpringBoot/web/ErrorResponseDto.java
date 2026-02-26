package com.example.SpringBoot.web;

import java.time.LocalDateTime;
//специальный класс, который описывает ошибку и используется в классе GlobalExceptionHandler
public record ErrorResponseDto(
        String message,

        String detailedMessage,

        LocalDateTime errorTime
) {
}
