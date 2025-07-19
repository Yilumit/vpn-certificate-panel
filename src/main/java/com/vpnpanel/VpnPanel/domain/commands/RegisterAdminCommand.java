package com.vpnpanel.VpnPanel.domain.commands;

import java.util.Set;

public record RegisterAdminCommand(
    String name,
    String nickname,
    String email,
    Set<String> roles
) {}
