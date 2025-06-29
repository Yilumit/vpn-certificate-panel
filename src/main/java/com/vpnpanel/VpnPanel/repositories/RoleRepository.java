package com.vpnpanel.VpnPanel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByName(RoleType name);
}
