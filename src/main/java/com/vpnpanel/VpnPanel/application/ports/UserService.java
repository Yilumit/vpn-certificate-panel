package com.vpnpanel.VpnPanel.application.ports;

import java.util.List;

import com.vpnpanel.VpnPanel.domain.models.User;

public interface UserService {

    User createUser(User user);

    User findByNickname(String nickname);
    User findByEmail(String email);
    User findById(Long id);

    List<User> listAllUsers();

    User increaseFailedLoginAttempts(User user);
    User resetFailedLoginAttempts(User user);

    User activateUser(String nickname);
    User deactivateUser(String nickname);
    User promoteToAdmin(String nickname);
    User demoteFromAdmin(String nickname);

    void deleteUser(String nickname);

    User changePassword(User user, String newPassword);

}
