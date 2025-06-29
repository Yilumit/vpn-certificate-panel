package com.vpnpanel.VpnPanel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.domain.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
