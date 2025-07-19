package com.vpnpanel.VpnPanel.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.vpnpanel.VpnPanel.application.ports.in.AuthUseCase;
import com.vpnpanel.VpnPanel.application.ports.out.AuthenticationPort;
import com.vpnpanel.VpnPanel.application.ports.out.FrontendUrlProviderPort;
import com.vpnpanel.VpnPanel.application.ports.out.MailPort;
import com.vpnpanel.VpnPanel.application.ports.out.PasswordEncoderPort;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.ConfirmationTokenPersistencePort;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.RolePersistencePort;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.UserPersistencePort;
import com.vpnpanel.VpnPanel.config.UseCase;
import com.vpnpanel.VpnPanel.domain.commands.AuthTokenResponse;
import com.vpnpanel.VpnPanel.domain.commands.CompleteRegistrationCommand;
import com.vpnpanel.VpnPanel.domain.commands.LoginCommand;
import com.vpnpanel.VpnPanel.domain.commands.RegisterAdminCommand;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.exceptions.ExpiredTokenException;
import com.vpnpanel.VpnPanel.domain.exceptions.InvalidCredentialsException;
import com.vpnpanel.VpnPanel.domain.exceptions.UserNotFoundException;
import com.vpnpanel.VpnPanel.domain.exceptions.ValidatePasswordException;
import com.vpnpanel.VpnPanel.domain.models.ConfirmationToken;
import com.vpnpanel.VpnPanel.domain.models.Role;
import com.vpnpanel.VpnPanel.domain.models.User;

@UseCase
public class AuthUseCaseService implements AuthUseCase {

    private final MailPort mailPort;
    private final AuthenticationPort authPort;
    private final FrontendUrlProviderPort frontUrlProvider;
    private final PasswordEncoderPort passwordEncoder;
    private final UserPersistencePort userPersistence;
    private final RolePersistencePort rolePersistence;
    private final ConfirmationTokenPersistencePort tokenPersistence;

    public AuthUseCaseService(AuthenticationPort authPort, UserPersistencePort userPersistence,
            RolePersistencePort rolePersistence, ConfirmationTokenPersistencePort tokenPersistence, MailPort mailPort,
            PasswordEncoderPort passwordEncoder, FrontendUrlProviderPort frontUrlProvider) {
        this.authPort = authPort;
        this.frontUrlProvider = frontUrlProvider;
        this.passwordEncoder = passwordEncoder;
        this.userPersistence = userPersistence;
        this.rolePersistence = rolePersistence;
        this.tokenPersistence = tokenPersistence;
        this.mailPort = mailPort;
    }

    @Override
    public AuthTokenResponse login(LoginCommand request) {
        if (!userPersistence.existsByNickname(request.nickname())) {
            throw new UserNotFoundException("Usuario nao encontrado");
        }

        try {
            String token = authPort.authenticate(request.nickname(), request.password());
            User user = userPersistence.findByNickname(request.nickname());
            List<String> roles = user.getRoles().stream().map(role -> role.getName().name()).toList();
            
            return new AuthTokenResponse(token, authPort.getExpirationMillis(), roles);
        } catch (Exception e) {
            throw new InvalidCredentialsException("Credenciais invalidas");
        }

    }

    @Override
    public void registerUserByAdmin(RegisterAdminCommand request) {
        if (userPersistence.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("Nome de usuário já está em uso!");
        }
        if (userPersistence.existsByEmail(request.email())) {
            throw new IllegalStateException("Este e-mail já está em uso");
        }

        Set<Role> roles = request.roles().stream()
                .map(role -> RoleType.valueOf(role.toUpperCase()))
                .map(roleType -> rolePersistence.findByName(roleType))
                .collect(Collectors.toSet());

        User user = new User();
        user.setName(request.name());
        user.setNickname(request.nickname());
        user.setEmail(request.email());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(false);

        userPersistence.save(user);
        tokenPersistence.deleteByUser(user);

        String link = frontUrlProvider.buildCompletionLink(
                tokenPersistence.createToken(user), "complete-registration");

        mailPort.send(request.email(), "Finalização de Cadastro",
                mailPort.createFinalizationEmail(request.name(), link));
    }

    @Override
    public void completeRegistration(CompleteRegistrationCommand request) {
        ConfirmationToken token = tokenPersistence.findByToken(request.token());

        if (!token.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new ExpiredTokenException("Token expirado! Solicite um novo ao administrador.");
        }

        User user = token.getUser();

        if (user.isActive() && !user.getPassword().isBlank() && user.getPassword() != null) {
            throw new IllegalArgumentException("Usuário já está ativo");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new ValidatePasswordException("As senhas não coincidem!");
        }

        validatePasswordRules(request.password());

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        
        userPersistence.save(user);
        tokenPersistence.delete(token);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user = userPersistence.findByEmail(email);

        tokenPersistence.deleteByUser(user);

        String link = frontUrlProvider.buildCompletionLink(
                tokenPersistence.createToken(user),
                "reset-password");

        mailPort.send(email, "Redefinição de Senha",
                mailPort.createResetEmail(email, link));
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        ConfirmationToken confirmationToken = tokenPersistence.findByToken(token);

        if (!confirmationToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new ExpiredTokenException("Token expirado! Solicite um novo ao administrador.");
        }

        validatePasswordRules(newPassword);

        User user = confirmationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);

        userPersistence.save(user);
        tokenPersistence.delete(confirmationToken);
    }

    private void validatePasswordRules(String password) {
        if (password.length() < 8) {
            throw new ValidatePasswordException("A senha deve conter no mínimo 8 caracteres");
        }
        // validacao por regex
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidatePasswordException("A senha deve conter pelo menos uma letra MAIÚSCULA");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new ValidatePasswordException("A senha deve conter pelo menos uma letra minúscula");
        }
        if (!password.matches(".*[@!#$%^&()_.-<>?~{}\\[\\]|\\\\/].*")) {
            throw new ValidatePasswordException("A senha deve conter pelo menos um caractere especial");
        }

    }

}
