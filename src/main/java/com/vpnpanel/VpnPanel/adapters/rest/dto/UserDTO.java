package com.vpnpanel.VpnPanel.adapters.rest.dto;

import com.vpnpanel.VpnPanel.domain.models.User;

public record UserDTO(String name, String nickname, String email) {
    
    public UserDTO(User user) {
        this(user.getName(), user.getNickname(), user.getEmail());
    }
}
