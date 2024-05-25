package com.thangkl2420.server_ducky.exception;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        ApiResponse<Object> response;
        if (ex instanceof UnauthorizedException) {
            response = new ApiResponse<>(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null, 0);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (ex instanceof BadRequestException) {
            response = new ApiResponse<>(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", null, 0);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof InternalServerErrorException) {
            response = new ApiResponse<>(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, 0);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else  if (ex instanceof BadGatewayException){
            response = new ApiResponse<>(LocalDateTime.now(), HttpStatus.BAD_GATEWAY.value(), "Bad Gateway", null, 0);
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        } else {
            response = new ApiResponse<>(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi không xác định", null, 0);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
