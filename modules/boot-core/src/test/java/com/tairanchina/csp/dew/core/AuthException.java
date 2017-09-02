package com.tairanchina.csp.dew.core;

public class AuthException extends RuntimeException {

    private String code;

    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }

}
