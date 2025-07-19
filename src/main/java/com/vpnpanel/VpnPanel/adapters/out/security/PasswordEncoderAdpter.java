package com.vpnpanel.VpnPanel.adapters.out.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.application.ports.out.PasswordEncoderPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdpter implements PasswordEncoderPort{

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
}
