package com.vpnpanel.VpnPanel.application.ports.out.persistence;

import java.util.List;

import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

public interface CertificatePersistencePort {
    Certificate findByFile(String file);

    Certificate save(Certificate certificate);
    void delete(Certificate certificate);

    List<Certificate> findByUser(User user);
    List<Certificate> findByUserAndStatus(User user, CertificateStatus status);
    List<Certificate> findByStatus(CertificateStatus status);
}
