package com.vpnpanel.VpnPanel.application.ports.out.persistence;

import com.vpnpanel.VpnPanel.domain.models.ConfirmationToken;
import com.vpnpanel.VpnPanel.domain.models.User;

public interface ConfirmationTokenPersistencePort {
    ConfirmationToken findByToken(String token);

    void delete(ConfirmationToken token);
    void deleteByUser(User user);

    String createToken(User user);
}
