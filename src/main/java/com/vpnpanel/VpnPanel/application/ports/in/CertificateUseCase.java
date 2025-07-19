package com.vpnpanel.VpnPanel.application.ports.in;

import java.util.List;

import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

public interface CertificateUseCase {

    List<Certificate> listCertificates(User user);

    List<Certificate> listByStatus(User user, CertificateStatus status);

    Certificate createCertificate(User user);

    Certificate revokeCertificate(User user, String certificateFile);

    void deleteCertificate(Certificate certificate);

    byte[] downloadCertificate(User user, String filename);
}
