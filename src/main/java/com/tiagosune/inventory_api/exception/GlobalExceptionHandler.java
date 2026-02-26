package com.tiagosune.inventory_api.exception;

import com.tiagosune.inventory_api.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException (BusinessException e,
                                                             HttpServletRequest request) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(java.time.Instant.now().toString());
        apiError.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(e.getMessage());
        apiError.setPath(request.getRequestURI());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException (ResourceNotFoundException e,
                                                                     HttpServletRequest request) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(java.time.Instant.now().toString());
        apiError.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setMessage(e.getMessage());
        apiError.setPath(request.getRequestURI());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException (Exception e, HttpServletRequest request) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(java.time.Instant.now().toString());
        apiError.setError("Internal Server Error");
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setMessage("Unexpected error occurred");
        apiError.setPath(request.getRequestURI());
        log.error("Unexpected error occurred", e);

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

}
