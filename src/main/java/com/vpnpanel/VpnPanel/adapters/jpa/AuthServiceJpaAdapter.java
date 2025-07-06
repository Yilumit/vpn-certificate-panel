package com.vpnpanel.VpnPanel.adapters.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpnpanel.VpnPanel.adapters.rest.dto.AdminRegisterRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.CompleteRegistrationRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.LoginRequestDTO;
import com.vpnpanel.VpnPanel.adapters.rest.dto.TokenResponseDTO;
import com.vpnpanel.VpnPanel.adapters.security.jwt.JwtProvider;
import com.vpnpanel.VpnPanel.application.ports.AuthService;
import com.vpnpanel.VpnPanel.application.ports.MailService;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.ConfirmationToken;
import com.vpnpanel.VpnPanel.domain.models.Role;
import com.vpnpanel.VpnPanel.domain.models.User;
import com.vpnpanel.VpnPanel.repositories.ConfirmationTokenRepository;
import com.vpnpanel.VpnPanel.repositories.RoleRepository;
import com.vpnpanel.VpnPanel.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceJpaAdapter implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend.base-url}")
    private String baseUrl;

    @Override
    public TokenResponseDTO login(LoginRequestDTO request) {
        if (!userRepository.existsByNickname(request.nickname())) {
            throw new BadCredentialsException("Usuário não encontrado!");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.nickname(), request.password()));

            String token = jwtProvider.generateToken(authentication);
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();

            return new TokenResponseDTO(token, jwtProvider.getExpirationMillis(), roles);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("As credenciáis passadas são inválidas!");
        }

    }

    @Override
    public void registerUserByAdmin(AdminRegisterRequestDTO request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("Nome de usuário já está em uso!");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Este e-mail já está em uso");
        }

        Set<Role> roles = request.roles().stream()
                .map(role -> RoleType.valueOf(role.toUpperCase()))
                .map(roleType -> roleRepository.findByName(roleType)
                        .orElseThrow(() -> new BadCredentialsException("Papel não encontrado: " + roleType)))
                .collect(Collectors.toSet());

        User user = new User();
        user.setName(request.name());
        user.setNickname(request.nickname());
        user.setEmail(request.email());
        user.setRoles(roles);
        user.setPassword(null);
        user.setActive(false);

        userRepository.save(user);
        // Remove qualquer token antigo que o usuário possa ter
        confirmationTokenRepository.deleteByUser(user);

        String link = buildCompletionLink( createConfirmationToken(user) );

        mailService.send(request.email(), "Finalização de Cadastro", mailService.createFinalizationEmail(request.name(), link));

    }

    private String buildCompletionLink(String token) {
        return baseUrl + "/complete-registration?token=" + token;
    }

    private String createConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, user, LocalDateTime.now().plusHours(12));

        confirmationTokenRepository.save(confirmationToken);

        return token;
    }

    @Override
    public void completeRegistration(CompleteRegistrationRequestDTO request) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou inexistente."));
        
        if (token.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado! Solicite um novo ao administrador.");
        }

        User user = token.getUser();

        if (user.isActive() && !user.getPassword().isBlank() && user.getPassword() != null) {
            throw new IllegalArgumentException("Usuário já está ativo");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("As senhas não coincidem!");
        }

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);

        userRepository.save(user);
        confirmationTokenRepository.delete(token);
    }

}
