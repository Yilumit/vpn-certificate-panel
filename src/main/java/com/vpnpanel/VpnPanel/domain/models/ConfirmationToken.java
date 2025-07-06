package com.vpnpanel.VpnPanel.domain.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {
    
    @Id
    private String token;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
