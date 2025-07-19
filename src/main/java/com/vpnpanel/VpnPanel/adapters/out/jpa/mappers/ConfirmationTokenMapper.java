package com.vpnpanel.VpnPanel.adapters.out.jpa.mappers;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.ConfirmationTokenEntity;
import com.vpnpanel.VpnPanel.domain.models.ConfirmationToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfirmationTokenMapper {

    private final UserMapper userMapper;
    
    public ConfirmationToken toDomain(ConfirmationTokenEntity entity) {
        if (entity == null) return null;
        return new ConfirmationToken(
            entity.getToken(), 
            entity.getExpiresAt(),
            userMapper.toDomain(entity.getUser())
        );
    }

    public ConfirmationTokenEntity toEntity(ConfirmationToken domain) {
        if (domain == null) return null;
        
        ConfirmationTokenEntity entity = new ConfirmationTokenEntity();
        entity.setToken(domain.getToken());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUser(userMapper.toEntity(domain.getUser()));

        return entity;
    }
}
