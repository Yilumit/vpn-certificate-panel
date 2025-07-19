package com.vpnpanel.VpnPanel.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpnpanel.VpnPanel.adapters.in.rest.dto.AdminRegisterRequestDTO;
import com.vpnpanel.VpnPanel.adapters.in.rest.dto.CompletePasswordRequestDTO;
import com.vpnpanel.VpnPanel.adapters.in.rest.dto.ForgotPasswordRequestDTO;
import com.vpnpanel.VpnPanel.adapters.in.rest.dto.LoginRequestDTO;
import com.vpnpanel.VpnPanel.application.ports.in.AuthUseCase;
import com.vpnpanel.VpnPanel.domain.commands.AuthTokenResponse;
import com.vpnpanel.VpnPanel.domain.commands.CompleteRegistrationCommand;
import com.vpnpanel.VpnPanel.domain.commands.LoginCommand;
import com.vpnpanel.VpnPanel.domain.commands.RegisterAdminCommand;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCase auth;

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(
            auth.login(new LoginCommand(request.nickname(), request.password()))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid AdminRegisterRequestDTO request) {
        auth.registerUserByAdmin(
                new RegisterAdminCommand(request.name(), request.nickname(), request.email(), request.roles()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete-registration")
    public ResponseEntity<Void> completeRegistration(@RequestBody @Valid CompletePasswordRequestDTO request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("As senhas não coincidem!");
        }
        auth.completeRegistration(
                new CompleteRegistrationCommand(request.token(), request.password(), request.confirmPassword()));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO request) {
        auth.sendPasswordResetEmail(request.email());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid CompletePasswordRequestDTO request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("As senhas não coincidem!");
        }
        auth.resetPassword(request.token(), request.password());

        return ResponseEntity.ok().build();
    }
}
