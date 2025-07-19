package com.vpnpanel.VpnPanel.domain.exceptions;

public class ValidatePasswordException extends IllegalArgumentException{
    public ValidatePasswordException(String message) {
        super(message);
    }

    public ValidatePasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
