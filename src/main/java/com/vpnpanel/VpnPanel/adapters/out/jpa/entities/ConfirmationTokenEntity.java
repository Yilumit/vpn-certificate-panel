package com.vpnpanel.VpnPanel.adapters.out.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "confiermation_tokens")
public class ConfirmationTokenEntity {
    
    @Id
    private String token;

    @OneToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

}
