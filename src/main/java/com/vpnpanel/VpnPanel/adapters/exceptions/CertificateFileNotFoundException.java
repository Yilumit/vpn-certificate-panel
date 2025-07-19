package com.vpnpanel.VpnPanel.adapters.exceptions;

public class CertificateFileNotFoundException extends RuntimeException {
    public CertificateFileNotFoundException(String message) {
        super(message);
    }

    public CertificateFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
