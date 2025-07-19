package com.vpnpanel.VpnPanel.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
