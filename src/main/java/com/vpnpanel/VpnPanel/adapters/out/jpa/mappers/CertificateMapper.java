package com.vpnpanel.VpnPanel.adapters.out.jpa.mappers;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.CertificateEntity;
import com.vpnpanel.VpnPanel.domain.models.Certificate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CertificateMapper {

    private final UserMapper userMapper;

    public Certificate toDomain(CertificateEntity entity) {
        if (entity == null)
            return null;

        return new Certificate(
                entity.getId(),
                entity.getFile(),
                entity.getStatus(),
                userMapper.toDomain(entity.getUser()),
                entity.getCreatedAt(),
                entity.getExpiresAt());
    }

    public CertificateEntity toEntity(Certificate domain) {
        if (domain == null)
            return null;

        CertificateEntity entity = new CertificateEntity();
        entity.setId(domain.getId());
        entity.setFile(domain.getfile());
        entity.setStatus(domain.getStatus());
        entity.setUser(userMapper.toEntity(domain.getUser()));
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setExpiresAt(domain.getExpiresAt());

        return entity;
    }
}
