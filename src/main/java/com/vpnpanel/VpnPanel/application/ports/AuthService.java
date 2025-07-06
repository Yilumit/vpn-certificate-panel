package com.vpnpanel.VpnPanel.application.ports;

import com.vpnpanel.VpnPanel.adapters.rest.dto.AdminRegisterRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.CompleteRegistrationRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.LoginRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.TokenResponseDTO;

public interface AuthService {

    TokenResponseDTO login(LoginRequestDTO request);

    void registerUserByAdmin(AdminRegisterRequestDTO request);
    void completeRegistration(CompleteRegistrationRequestDTO request);

}
