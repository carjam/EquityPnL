package com.companyx.equity.error;

public class ResponseVerificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResponseVerificationException(String errorMessage) { super(errorMessage); }
}
