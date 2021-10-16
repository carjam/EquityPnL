package com.companyx.equity.error;

public class UnexpectedValueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedValueException(String errorMessage) { super(errorMessage); }
}
