package com.companyx.equity.error;

public class UnexpectedPriceCountException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedPriceCountException(String errorMessage) { super(errorMessage); }
}
