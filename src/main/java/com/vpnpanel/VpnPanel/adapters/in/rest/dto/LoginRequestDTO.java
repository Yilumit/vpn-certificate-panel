package com.vpnpanel.VpnPanel.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "O usuário não pode estar vazio")
    String nickname,

    @NotBlank(message = "A senha não deve estar vazia")
    String password) {}
