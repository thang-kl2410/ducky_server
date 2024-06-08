package com.thangkl2420.server_ducky.exception;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {
    private LocalDateTime timestamp;
    private int code;
    private String message;
    private T data;
    private int total;
}
