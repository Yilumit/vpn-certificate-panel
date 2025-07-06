package com.vpnpanel.VpnPanel.application.ports;

import java.util.List;
import java.util.Optional;

import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

public interface CertificateService {
    
    Optional<Certificate> findByFile(String file);

    List<Certificate> listCertificates(User user);
    List<Certificate> listByUserAndActive(User user);
    List<Certificate> listByUserAndRevoked(User user);
    List<Certificate> listByUserAndExpired(User user);

    Certificate createCertificate(User user);
    Certificate revokeCertificate(User user, String certificateFile);
    void deleteCertificate(Certificate certificate);
    
}
