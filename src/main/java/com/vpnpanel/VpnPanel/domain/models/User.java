package com.vpnpanel.VpnPanel.domain.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.vpnpanel.VpnPanel.domain.enums.RoleType;

public class User {
    private Long id;

    private String name;
    private String email;
    private String nickname;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Set<Role> roles = new HashSet<>();

    private boolean active;

    private int failedLoginAttempts;

    public User() {
    }

    public static User createWithoutPassword(Long id, String name, String email, String nickname,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            Set<Role> roles) {

        User user = new User();
        user.id = id;
        user.name = name;
        user.email = email;
        user.nickname = nickname;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        user.roles = roles;
        user.active = false;
        user.failedLoginAttempts = 0;

        return user;
    }

    public static User createWithPassword(Long id, String name, String email, String nickname, String password,
            LocalDateTime createdAt,
            LocalDateTime updatedAt, Set<Role> roles, boolean active, int failedLoginAttempts) {

        User user = createWithoutPassword(id, name, email, nickname, createdAt, updatedAt, roles);
        user.password = password;
        user.active = active;
        user.failedLoginAttempts = failedLoginAttempts;

        return user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUpdatedAt(LocalDateTime now) {
        this.updatedAt = now;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }

    public boolean hasRole(RoleType roleType) {
        return roles.stream().anyMatch(r -> r.getName() == roleType);
    }

}
