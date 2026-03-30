package com.healthcare.system.exception;

import com.healthcare.system.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
    }
}