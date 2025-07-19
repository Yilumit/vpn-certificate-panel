package com.vpnpanel.VpnPanel.adapters.out.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.RoleEntity;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

    Optional<RoleEntity> findByName(RoleType name);
}
