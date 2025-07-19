package com.vpnpanel.VpnPanel.domain.models;

import java.time.LocalDateTime;

public class ConfirmationToken {

    private String token;

    private LocalDateTime expiresAt;

    private User user;

    public ConfirmationToken() {}

    public ConfirmationToken(String token, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public String getToken() { return token; }
    public void setToken(String token) {this.token = token;}

    public LocalDateTime getExpiresAt() {return expiresAt;}
    public void setExpiresAt(LocalDateTime expiresAt) {this.expiresAt = expiresAt;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

}
