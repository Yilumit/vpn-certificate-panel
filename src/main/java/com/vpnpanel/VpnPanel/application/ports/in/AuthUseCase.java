package com.vpnpanel.VpnPanel.application.ports.in;

import com.vpnpanel.VpnPanel.domain.commands.AuthTokenResponse;
import com.vpnpanel.VpnPanel.domain.commands.CompleteRegistrationCommand;
import com.vpnpanel.VpnPanel.domain.commands.LoginCommand;
import com.vpnpanel.VpnPanel.domain.commands.RegisterAdminCommand;

public interface AuthUseCase {
    AuthTokenResponse login(LoginCommand request);

    void registerUserByAdmin(RegisterAdminCommand request);
    void completeRegistration(CompleteRegistrationCommand request);
    void sendPasswordResetEmail(String email);
    void resetPassword(String token, String newPassword);

}
