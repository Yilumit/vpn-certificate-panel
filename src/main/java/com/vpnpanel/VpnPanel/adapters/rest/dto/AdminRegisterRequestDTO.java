package com.vpnpanel.VpnPanel.adapters.rest.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record AdminRegisterRequestDTO(
    @NotBlank(message = "Nome é Obrigatório")
    String name, 
    
    @NotBlank(message = "O usário de login não pode estar vazio")
    String nickname, 
    
    @NotBlank(message = "O e-mail é obirgatório")
    String email, 
    
    Set<String> roles
) {}
