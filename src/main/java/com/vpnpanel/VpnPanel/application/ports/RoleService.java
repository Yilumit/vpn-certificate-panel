package com.vpnpanel.VpnPanel.application.ports;

import java.util.Optional;

import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.Role;

public interface RoleService {
    
    Optional<Role> findByName(RoleType name);
}
