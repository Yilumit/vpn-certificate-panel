package com.vpnpanel.VpnPanel.domain.models;

import java.time.LocalDateTime;

import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;

public class Certificate {
    private Long id;

    private String file;

    private CertificateStatus status;
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Certificate() {}

    public Certificate(Long id, String file, CertificateStatus status, User user, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id = id;
        this.file = file;
        this.status = status;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getfile() { return file; }
    public void setfile(String file) { this.file = file; }

    public CertificateStatus getStatus() { return status; }
    public void setStatus(CertificateStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public void onCreate() {
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusDays(7);
        status = CertificateStatus.ACTIVE;
    }

}
