package com.vpnpanel.VpnPanel.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>{

    Optional<Certificate> findByFile(String file);

    List<Certificate> findByUser(User user);
    List<Certificate> findByUserAndStatus(User user, CertificateStatus status);
    List<Certificate> findByStatus(CertificateStatus status);

}
