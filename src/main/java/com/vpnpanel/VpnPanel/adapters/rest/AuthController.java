package com.vpnpanel.VpnPanel.adapters.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpnpanel.VpnPanel.adapters.rest.dto.AdminRegisterRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.CompleteRegistrationRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.LoginRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.TokenResponseDTO;
import com.vpnpanel.VpnPanel.application.ports.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid AdminRegisterRequestDTO request) {
        authService.registerUserByAdmin(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete-registration")
    public ResponseEntity<Void> completeRegistration(@RequestBody @Valid CompleteRegistrationRequestDTO request) {
        authService.completeRegistration(request);
        return ResponseEntity.ok().build();
    }

    // @PostMapping("/forgot-password")
    // public ResponseEntity<Void> 
}
