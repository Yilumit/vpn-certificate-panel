package com.vpnpanel.VpnPanel.adapters.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vpnpanel.VpnPanel.adapters.exceptions.VpnCertificateScriptException;
import com.vpnpanel.VpnPanel.application.ports.CertificateService;
import com.vpnpanel.VpnPanel.application.ports.VpnCertificateScriptService;
import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;
import com.vpnpanel.VpnPanel.repositories.CertificateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificateServiceJpaAdapter implements CertificateService {
    
    private final CertificateRepository certificateRepository;
    private final VpnCertificateScriptService vpnCertificateScriptService;

    @Override
    public Optional<Certificate> findByFile(String file){
        return certificateRepository.findByFile(file);
    }

    @Override
    @Transactional
    public Certificate createCertificate(User user){

        try {
            String fileGenerated = vpnCertificateScriptService.createVpnCertificate(user.getNickname());
            Certificate certificate = Certificate.builder()
                .file(fileGenerated)
                .user(user)
                .build();
    
            return certificateRepository.save(certificate);

        } catch (Exception e) {
            throw new VpnCertificateScriptException("Erro ao criar o certificado para o usuário: " + user.getNickname(), e);
        }
    }

    @Override
    @Transactional
    public Certificate revokeCertificate(User user, String certificateFile) {
        Certificate certificate = certificateRepository.findByFile(certificateFile)
            .orElseThrow(() -> new VpnCertificateScriptException("Certificado não encontrado para o usuário: " + user.getNickname()));
        if (certificate.getUser() == null || !certificate.getUser().getNickname().equals(user.getNickname())) {
            throw new VpnCertificateScriptException("Certificado não pertence ao usuário: " + user.getNickname());
        }
        if (certificate.getStatus() == CertificateStatus.EXPIRED) {
            throw new VpnCertificateScriptException("Certificado já está expirado para o usuário: " + user.getNickname());
        }
        if (certificate.getStatus() == CertificateStatus.REVOKED) {
            throw new VpnCertificateScriptException("Certificado já está revogado para o usuário: " + user.getNickname());
        }
        try {
            vpnCertificateScriptService.revokeVpnCertificate(user.getNickname(), certificate.getFile());
            certificate.setStatus(CertificateStatus.REVOKED);
            
            return certificateRepository.save(certificate);
        } catch (Exception e) {
            throw new VpnCertificateScriptException("Erro ao revogar o certificado para o usuário: " + user.getNickname(), e);
        }
    }

    @Override
    @Transactional
    public void deleteCertificate(Certificate certificate) {
        
        if (certificate == null || certificate.getId() == null) {
            throw new IllegalArgumentException("Certificado inválido para exclusão.");
        }
        if (certificate.getStatus() != CertificateStatus.REVOKED) {
            try {
                vpnCertificateScriptService.revokeVpnCertificate(certificate.getUser().getNickname(), certificate.getFile());
            } catch (Exception e) {
                throw new VpnCertificateScriptException("Erro ao excluir o certificado: " + certificate.getFile(), e);
            }
        }
        certificateRepository.delete(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificate> listCertificates(User user) {
        return certificateRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificate> listByUserAndActive(User user) {
        return certificateRepository.findByUserAndStatus(user, CertificateStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificate> listByUserAndRevoked(User user) {
        return certificateRepository.findByUserAndStatus(user, CertificateStatus.REVOKED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Certificate> listByUserAndExpired(User user) {
        return certificateRepository.findByUserAndStatus(user, CertificateStatus.EXPIRED);
    }  



}
