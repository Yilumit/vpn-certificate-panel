package com.vpnpanel.VpnPanel.adapters.out.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.application.ports.out.FrontendUrlProviderPort;

@Component
public class FrontendUrlProviderAdapter implements FrontendUrlProviderPort{

    @Value("${app.frontend.base-url}")
    private String baseUrl;
    
    @Override
    public String buildCompletionLink(String token, String path) {
        return baseUrl + "/auth/" + path + "?token=" + token;
    }
    
}
