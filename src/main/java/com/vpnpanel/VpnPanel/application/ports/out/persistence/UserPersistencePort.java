package com.vpnpanel.VpnPanel.application.ports.out.persistence;

import java.util.List;

import com.vpnpanel.VpnPanel.domain.models.User;

public interface UserPersistencePort {
    User findByNickname(String nickname);
    User findByEmail(String email);
    User findByName(String name);

    List<User> findAll();

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    User save(User user);
    void delete(User user);

}
