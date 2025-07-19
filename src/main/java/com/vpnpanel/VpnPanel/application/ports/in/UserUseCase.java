package com.vpnpanel.VpnPanel.application.ports.in;

import java.util.List;

import com.vpnpanel.VpnPanel.domain.models.User;

public interface UserUseCase {
    List<User> listAllUsers();

    User increaseFailedLoginAttempts(User user);

    User resetFailedLoginAttempts(User user);

    User activateUser(String nickname);

    User deactivateUser(String nickname);

    User promoteToAdmin(String nickname);

    User demoteFromAdmin(String nickname);

    void deleteUser(String nickname);

    void changePassword(User user, String newPassword);
}
