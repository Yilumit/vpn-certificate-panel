package com.vpnpanel.VpnPanel.application.ports.out;

public interface AuthenticationPort {
    String authenticate(String nickname, String password);

    long getExpirationMillis();
}
