package com.vpnpanel.VpnPanel.adapters.out.jpa.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.mappers.UserMapper;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.UserRepository;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.UserPersistencePort;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserPersistencePort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User findByNickname(String nickname) {
        return userMapper.toDomain(
                userRepository.findByNickname(nickname)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado")));
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.toDomain(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email nao encontrado")));
    }

    @Override
    public User findByName(String name) {
        return userMapper.toDomain(userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado")));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userMapper.toDomain(userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public void delete(User user) {
        userRepository.delete(userMapper.toEntity(user));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

}
