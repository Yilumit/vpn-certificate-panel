package com.vpnpanel.VpnPanel.adapters.rest.dto;

import java.util.List;

public record TokenResponseDTO(String token, Long expiresIn, List<String> roles) {}
