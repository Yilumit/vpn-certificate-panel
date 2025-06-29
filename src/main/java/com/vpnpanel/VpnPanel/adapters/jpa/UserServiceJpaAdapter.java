package com.vpnpanel.VpnPanel.adapters.jpa;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpnpanel.VpnPanel.application.ports.UserService;
import com.vpnpanel.VpnPanel.domain.models.User;
import com.vpnpanel.VpnPanel.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceJpaAdapter implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void increaseFailedLoginAttempts(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);

        //Se verdadeiro, revoga o acesso
        if (attempts >= 10) {
            user.setActive(false);
        }

        userRepository.save(user);
    }

    @Override
    public void resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public void activateUser(User user) {
        user.setActive(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(User user) {
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
