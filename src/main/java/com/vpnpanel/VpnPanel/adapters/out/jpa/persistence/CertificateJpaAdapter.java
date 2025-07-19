package com.vpnpanel.VpnPanel.adapters.out.jpa.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.exceptions.CertificateFileNotFoundException;
import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.CertificateEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.mappers.CertificateMapper;
import com.vpnpanel.VpnPanel.adapters.out.jpa.mappers.UserMapper;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.CertificateRepository;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.CertificatePersistencePort;
import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CertificateJpaAdapter implements CertificatePersistencePort {

    private final CertificateRepository certificateRepository;
    private final CertificateMapper certificateMapper;
    private final UserMapper userMapper;

    @Override
    public Certificate findByFile(String file) {
        CertificateEntity entity = certificateRepository.findByFile(file)
                .orElseThrow(() -> new CertificateFileNotFoundException("Certificado nao encontrado"));

        return certificateMapper.toDomain(entity);
    }

    @Override
    public Certificate save(Certificate certificate) {
        return certificateMapper.toDomain(certificateRepository.save(
                certificateMapper.toEntity(certificate)));
    }

    @Override
    public void delete(Certificate certificate) {
        certificateRepository.delete(certificateMapper.toEntity(certificate));
    }

    @Override
    public List<Certificate> findByUser(User user) {
        return certificateRepository.findByUser(userMapper.toEntity(user)).stream()
                .map(certificateMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificate> findByUserAndStatus(User user, CertificateStatus status) {
        return certificateRepository.findByUserAndStatus(userMapper.toEntity(user), status).stream()
                .map(certificateMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificate> findByStatus(CertificateStatus status) {
        return certificateRepository.findByStatus(status).stream()
                .map(certificateMapper::toDomain)
                .collect(Collectors.toList());
    }

}
