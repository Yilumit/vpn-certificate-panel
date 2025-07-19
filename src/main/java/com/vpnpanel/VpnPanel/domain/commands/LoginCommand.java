package com.vpnpanel.VpnPanel.domain.commands;

public record LoginCommand(
    String nickname,
    String password
) {}
