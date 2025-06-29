package com.vpnpanel.VpnPanel.application.ports;

import java.util.Optional;

import com.vpnpanel.VpnPanel.domain.models.User;

public interface UserService {

    User createUser(User user);

    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    void increaseFailedLoginAttempts(User user);
    void resetFailedLoginAttempts(User user);
    void activateUser(User user);
    void deactivateUser(User user);

    void changePassword(User user, String newPassword);

}
