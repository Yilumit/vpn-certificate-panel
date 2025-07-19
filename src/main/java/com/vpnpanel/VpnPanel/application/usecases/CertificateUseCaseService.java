package com.vpnpanel.VpnPanel.application.usecases;

import java.util.List;

import com.vpnpanel.VpnPanel.application.ports.in.CertificateUseCase;
import com.vpnpanel.VpnPanel.application.ports.out.VpnCertificateScriptPort;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.CertificatePersistencePort;
import com.vpnpanel.VpnPanel.config.UseCase;
import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.exceptions.InvalidCertificateException;
import com.vpnpanel.VpnPanel.domain.exceptions.UnauthorizadUserException;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

@UseCase
public class CertificateUseCaseService implements CertificateUseCase {

    private final CertificatePersistencePort certificatePersistence;
    private final VpnCertificateScriptPort vpnScript;

    public CertificateUseCaseService(CertificatePersistencePort certificatePersistence,
            VpnCertificateScriptPort vpnScript) {
        this.certificatePersistence = certificatePersistence;
        this.vpnScript = vpnScript;
    }

    @Override
    public List<Certificate> listCertificates(User user) {
        return certificatePersistence.findByUser(user);
    }

    @Override
    public List<Certificate> listByStatus(User user, CertificateStatus status) {
        return certificatePersistence.findByUserAndStatus(user, status);
    }

    @Override
    public Certificate createCertificate(User user) {
        try {
            String fileGenerated = vpnScript.createVpnCertificate(user.getNickname());

            Certificate cert = new Certificate();
            cert.setfile(fileGenerated);
            cert.setUser(user);
            cert.onCreate();

            return certificatePersistence.save(cert);

        } catch (Exception e) {
            throw new RuntimeException("Erro no servidor OpenVPN ao gerar o certificado", e);
        }
    }

    @Override
    public Certificate revokeCertificate(User user, String certificateFile) {
        Certificate cert = certificatePersistence.findByFile(certificateFile);

        if (cert.getUser() == null || !cert.getUser().getNickname().equals(user.getNickname())) {
            throw new UnauthorizadUserException("Certificado nao pertence ao usuario");
        }

        if (cert.getStatus() == CertificateStatus.EXPIRED) {
            throw new InvalidCertificateException("Certificado ja expirado");
        }

        if (cert.getStatus() == CertificateStatus.REVOKED) {
            throw new InvalidCertificateException("Certificado ja revogado");
        }

        try {
            vpnScript.revokeVpnCertificate(user.getNickname(), cert.getfile());
            cert.setStatus(CertificateStatus.REVOKED);
            return certificatePersistence.save(cert);
        } catch (Exception e) {
            throw new RuntimeException("Erro no servidor OpenVPN ao revogar o certificado", e);
        }
    }

    @Override
    public void deleteCertificate(Certificate certificate) {
        if (certificate == null || certificate.getId() == null) {
            throw new InvalidCertificateException("Certificado invalido para exclusao");
        }

        if (certificate.getStatus() != CertificateStatus.REVOKED
                && certificate.getStatus() != CertificateStatus.EXPIRED) {
            try {
                vpnScript.revokeVpnCertificate(
                        certificate.getUser().getNickname(),
                        certificate.getfile());
            } catch (Exception e) {
                throw new RuntimeException("Erro no servidor OpenVPN ao tentar revogar o certificado", e);
            }
        }

        certificatePersistence.delete(certificate);
    }

    @Override
    public byte[] downloadCertificate(User user, String filename) {
        Certificate cert = certificatePersistence.findByFile(filename);

        if (!cert.getUser().getId().equals(user.getId())) {
            throw new UnauthorizadUserException("Você não tem permissão para acessar este certificado");
        }

        return vpnScript.readCertificateFile(user.getNickname(), filename);
    }

}
