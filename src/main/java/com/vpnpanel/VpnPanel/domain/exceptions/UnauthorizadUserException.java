package com.vpnpanel.VpnPanel.domain.exceptions;

public class UnauthorizadUserException extends RuntimeException {
    public UnauthorizadUserException(String message) {
        super(message);
    }
}
