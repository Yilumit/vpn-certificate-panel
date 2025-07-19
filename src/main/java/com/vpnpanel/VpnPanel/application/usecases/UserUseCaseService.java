package com.vpnpanel.VpnPanel.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import com.vpnpanel.VpnPanel.application.ports.in.CertificateUseCase;
import com.vpnpanel.VpnPanel.application.ports.in.UserUseCase;
import com.vpnpanel.VpnPanel.application.ports.out.PasswordEncoderPort;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.RolePersistencePort;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.UserPersistencePort;
import com.vpnpanel.VpnPanel.config.UseCase;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.Role;
import com.vpnpanel.VpnPanel.domain.models.User;

@UseCase
public class UserUseCaseService implements UserUseCase {

    private final UserPersistencePort userPersistence;
    private final RolePersistencePort rolePersistence;
    private final CertificateUseCase certificateService;
    private final PasswordEncoderPort passwordEncoderPort;

    public UserUseCaseService(UserPersistencePort userPersistence, RolePersistencePort rolePersistence,
            CertificateUseCase certificateService, PasswordEncoderPort passwordEncoderPort) {
        this.userPersistence = userPersistence;
        this.rolePersistence = rolePersistence;
        this.certificateService = certificateService;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public List<User> listAllUsers() {
        return userPersistence.findAll();
    }

    @Override
    public User increaseFailedLoginAttempts(User user) {
        user.incrementFailedLoginAttempts();

        return userPersistence.save(user);
    }

    @Override
    public User resetFailedLoginAttempts(User user) {
        user.resetFailedLoginAttempts();

        return userPersistence.save(user);
    }

    @Override
    public User activateUser(String nickname) {
        return setActiveStatus(nickname, true);
    }

    @Override
    public User deactivateUser(String nickname) {
        return setActiveStatus(nickname, false);
    }

    @Override
    public User promoteToAdmin(String nickname) {
        User user = userPersistence.findByNickname(nickname);

        if (!user.hasRole(RoleType.ADMIN)) {
            Role adminRole = rolePersistence.findByName(RoleType.ADMIN);
            
            user.getRoles().add(adminRole);
            user.setUpdatedAt(LocalDateTime.now());
        } else {
            throw new IllegalStateException("Usuario ja e adinistrador");
        }

        return userPersistence.save(user);
    }

    @Override
    public User demoteFromAdmin(String nickname) {
        User user = userPersistence.findByNickname(nickname);

        user.getRoles().removeIf(role -> role.getName() == RoleType.ADMIN);
        user.setUpdatedAt(LocalDateTime.now());

        return userPersistence.save(user);
    }

    @Override
    public void deleteUser(String nickname) {
        User user = userPersistence.findByNickname(nickname);
        List<Certificate> certificates = certificateService.listCertificates(user);
        certificates.forEach(certificateService::deleteCertificate);

        userPersistence.delete(user);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoderPort.encode(newPassword));
    }

    private User setActiveStatus(String nickname, boolean active) {
        User user = userPersistence.findByNickname(nickname);
        user.setActive(active);
        if (active)
            user.resetFailedLoginAttempts();

        userPersistence.save(user);
        return user;
    }

}
