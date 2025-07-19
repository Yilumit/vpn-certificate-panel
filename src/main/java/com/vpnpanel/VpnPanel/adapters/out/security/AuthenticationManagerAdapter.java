package com.vpnpanel.VpnPanel.adapters.out.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.security.jwt.JwtProvider;
import com.vpnpanel.VpnPanel.application.ports.out.AuthenticationPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerAdapter implements AuthenticationPort {
    
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    
    @Override
    public String authenticate(String nickname, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(nickname, password));

            return jwtProvider.generateToken(authentication);
        } catch (Exception e) {
            throw new BadCredentialsException("Credenciais invalidas", e);
        }
    }

    @Override
    public long getExpirationMillis() {
        return jwtProvider.getExpirationMillis();
    }

}
