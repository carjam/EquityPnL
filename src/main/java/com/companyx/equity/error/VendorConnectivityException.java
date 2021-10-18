package com.companyx.equity.error;

public class VendorConnectivityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public VendorConnectivityException(String errorMessage) { super(errorMessage); }
}
