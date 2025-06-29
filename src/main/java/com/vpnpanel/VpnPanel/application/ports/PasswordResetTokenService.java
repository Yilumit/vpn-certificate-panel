package com.vpnpanel.VpnPanel.application.ports;

import java.util.Optional;

import com.vpnpanel.VpnPanel.domain.models.PasswordResetToken;

public interface PasswordResetTokenService {

    Optional<PasswordResetToken> findByToken(String token);
    
}
