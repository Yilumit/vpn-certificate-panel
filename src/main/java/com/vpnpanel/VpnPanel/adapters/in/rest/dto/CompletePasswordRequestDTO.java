package com.vpnpanel.VpnPanel.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompletePasswordRequestDTO(
    @NotBlank(message = "Obrigatório token de confirmação") 
    String token,

    @NotBlank(message = "A senha não deve estar vazia")
    @Size(min = 8, message = "A senha deve conter pleo menos 8 caracteres")
    String password,

    @NotBlank(message = "É necessário a confirmação da senha")
    String confirmPassword
    ) {
}
