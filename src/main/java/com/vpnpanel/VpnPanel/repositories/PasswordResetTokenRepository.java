package com.vpnpanel.VpnPanel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.domain.models.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
    
    Optional<PasswordResetToken> findByToken(String token);
}
