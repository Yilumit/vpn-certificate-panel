package com.vpnpanel.VpnPanel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vpnpanel.VpnPanel.domain.models.ConfirmationToken;
import com.vpnpanel.VpnPanel.domain.models.User;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    Optional<ConfirmationToken> findByToken(String token);
    void deleteByUser(User user);

}
