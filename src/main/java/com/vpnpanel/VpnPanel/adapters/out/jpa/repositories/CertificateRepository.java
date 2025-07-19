package com.vpnpanel.VpnPanel.adapters.out.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.CertificateEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.UserEntity;
import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, Long>{

    Optional<CertificateEntity> findByFile(String file);

    List<CertificateEntity> findByUser(UserEntity user);
    List<CertificateEntity> findByUserAndStatus(UserEntity user, CertificateStatus status);
    List<CertificateEntity> findByStatus(CertificateStatus status);

}
