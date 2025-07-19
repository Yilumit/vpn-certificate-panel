package com.vpnpanel.VpnPanel.adapters.out.jpa.mappers;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.RoleEntity;
import com.vpnpanel.VpnPanel.domain.models.Role;

@Component
public class RoleMapper {

    public Role toDomain(RoleEntity entity) {
        if (entity == null)
            return null;

        return new Role(
                entity.getId(),
                entity.getName());
    }

    public RoleEntity toEntity(Role domain) {
        if (domain == null) return null;

        RoleEntity entity = new RoleEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());

        return entity;
    }
}
