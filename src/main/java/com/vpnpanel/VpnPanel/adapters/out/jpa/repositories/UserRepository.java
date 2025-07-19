package com.vpnpanel.VpnPanel.adapters.out.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByNickname(String nickname);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByName(String name);

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
