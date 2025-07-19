package com.vpnpanel.VpnPanel.adapters.out.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.ConfirmationTokenEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.UserEntity;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, String> {

    Optional<ConfirmationTokenEntity> findByToken(String token);
    void deleteByUser(UserEntity user);

}
