package com.vpnpanel.VpnPanel.adapters.out.jpa.persistence;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.exceptions.RoleNotFoundException;
import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.RoleEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.mappers.RoleMapper;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.RoleRepository;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.RolePersistencePort;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleJpaAdapter implements RolePersistencePort {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Role findByName(RoleType name) {
        RoleEntity entity = roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Papel de usuario nao encontrado"));
        return roleMapper.toDomain(entity);
    }

    @Override
    public void save(Role name) {
        roleRepository.save(roleMapper.toEntity(name));
    }

}
