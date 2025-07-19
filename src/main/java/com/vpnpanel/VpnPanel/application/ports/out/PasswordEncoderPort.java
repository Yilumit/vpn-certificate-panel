package com.vpnpanel.VpnPanel.application.ports.out;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    // String matches(String rawPassword, String encodedPassword);
}
