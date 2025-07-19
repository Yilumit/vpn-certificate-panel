package com.vpnpanel.VpnPanel.application.ports.out;

public interface FrontendUrlProviderPort {
    String buildCompletionLink(String token, String path);
}
