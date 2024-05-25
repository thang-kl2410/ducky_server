package com.thangkl2420.server_ducky.exception;

public class BadGatewayException  extends RuntimeException {
    public BadGatewayException(String message) {
        super(message);
    }
}