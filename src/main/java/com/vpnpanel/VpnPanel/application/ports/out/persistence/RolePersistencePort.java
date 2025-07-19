package com.vpnpanel.VpnPanel.application.ports.out.persistence;

import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.Role;

public interface RolePersistencePort {
        Role findByName(RoleType name);

        void save(Role name) ;

}
