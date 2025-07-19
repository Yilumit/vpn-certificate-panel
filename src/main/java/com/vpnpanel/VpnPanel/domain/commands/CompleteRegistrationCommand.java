package com.vpnpanel.VpnPanel.domain.commands;

public record CompleteRegistrationCommand(
    String token,
    String password,
    String confirmPassword
) {}
