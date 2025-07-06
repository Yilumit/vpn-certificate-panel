package com.vpnpanel.VpnPanel.domain.models;

import java.time.LocalDateTime;

import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder //Evita o boilerplate e ajuda na visualizacao na instancia do objeto
@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto incrementa o ID
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) //Nao carrega os dados do usuario imediantamente, apenas quando explicitamente solicitado
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String file;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateStatus status;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusDays(7);
        status = CertificateStatus.ACTIVE;
    }

}
