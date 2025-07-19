package com.vpnpanel.VpnPanel.adapters.out.jpa.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.UserEntity;
import com.vpnpanel.VpnPanel.domain.models.Role;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public User toDomain(UserEntity entity) {
        if (entity == null)
            return null;

        Set<Role> roles = entity.getRoles().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toSet());

        return User.createWithPassword(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                roles,
                entity.isActive(),
                entity.getFailedLoginAttempts());
    }

    public UserEntity toEntity(User domain) {
        if (domain == null)
            return null;

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setEmail(domain.getEmail());
        entity.setName(domain.getName());
        entity.setNickname(domain.getNickname());
        entity.setPassword(domain.getPassword());

        entity.setRoles(domain.getRoles().stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet()));

        entity.setActive(domain.isActive());
        entity.setFailedLoginAttempts(domain.getFailedLoginAttempts());

        return entity;
    }
}
