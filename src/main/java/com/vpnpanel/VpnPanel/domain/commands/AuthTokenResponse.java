package com.vpnpanel.VpnPanel.domain.commands;

import java.util.List;

public record AuthTokenResponse(
    String token,
    long expiresIn,
    List<String> roles
) {}
