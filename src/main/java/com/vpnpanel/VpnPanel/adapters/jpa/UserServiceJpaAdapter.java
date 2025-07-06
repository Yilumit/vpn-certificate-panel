package com.vpnpanel.VpnPanel.adapters.jpa;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpnpanel.VpnPanel.application.ports.CertificateService;
import com.vpnpanel.VpnPanel.application.ports.UserService;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.Role;
import com.vpnpanel.VpnPanel.domain.models.User;
import com.vpnpanel.VpnPanel.repositories.RoleRepository;
import com.vpnpanel.VpnPanel.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceJpaAdapter implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CertificateService certificateService;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado " + nickname));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Não foi encontrado usuário com esse email " + email));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado"));
    }

    @Override
    public User increaseFailedLoginAttempts(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);

        // Se verdadeiro, revoga o acesso
        if (attempts >= 10) {
            user.setActive(false);
        }

        return userRepository.save(user);
    }

    @Override
    public User resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);

        return userRepository.save(user);

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
    public User changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));

        return userRepository.save(user);
    }

    @Override
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User promoteToAdmin(String nickname) {
        User user = findByNickname(nickname);

        if (user.getRoles().stream().noneMatch(role -> role.getName() == RoleType.ADMIN)) {
            Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Role ADMIN não foi encontrada"));

            user.getRoles().add(adminRole);
        } else {
            throw new IllegalStateException("O usuário já possui acesso de administrador");
        }

        return userRepository.save(user);
    }

    @Override
    public User demoteFromAdmin(String nickname) {
        User user = findByNickname(nickname);

        user.getRoles().removeIf(role -> role.getName() == RoleType.ADMIN);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String nickname) {
        User user = findByNickname(nickname);
        List<Certificate> certificates = certificateService.listCertificates(user);

        certificates.forEach(certificateService::deleteCertificate);

        userRepository.delete(user);
    }

    private User setActiveStatus(String nickname, boolean active) {
        User user = findByNickname(nickname);
        user.setActive(active);
        if (active)
            user.setFailedLoginAttempts(0);

        return userRepository.save(user);
    }

}
