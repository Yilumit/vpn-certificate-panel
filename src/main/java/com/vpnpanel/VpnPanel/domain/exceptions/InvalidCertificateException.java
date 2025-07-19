package com.vpnpanel.VpnPanel.domain.exceptions;

public class InvalidCertificateException extends RuntimeException{
    public InvalidCertificateException(String message) {
        super(message);
    }
}
