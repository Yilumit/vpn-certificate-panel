package com.vpnpanel.VpnPanel.adapters.out.jpa.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.exceptions.TokenNotFoundException;
import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.ConfirmationTokenEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.mappers.ConfirmationTokenMapper;
import com.vpnpanel.VpnPanel.adapters.out.jpa.mappers.UserMapper;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.ConfirmationTokenRepository;
import com.vpnpanel.VpnPanel.application.ports.out.persistence.ConfirmationTokenPersistencePort;
import com.vpnpanel.VpnPanel.domain.models.ConfirmationToken;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfirmationTokenJpaAdapter implements ConfirmationTokenPersistencePort {

    private final ConfirmationTokenRepository tokenRepository;
    private final ConfirmationTokenMapper tokenMapper;
    private final UserMapper userMapper;

    @Override
    public ConfirmationToken findByToken(String token) {
        ConfirmationTokenEntity entity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token de confirmacao nao encontrado"));

        return tokenMapper.toDomain(entity);
    }

    @Override
    public void deleteByUser(User user) {
        tokenRepository.deleteByUser(userMapper.toEntity(user));
    }

    @Override
    public String createToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(token,
                userMapper.toEntity(user),
                LocalDateTime.now().plusHours(12));

        tokenRepository.save(confirmationToken);

        return token;
    }

    @Override
    public void delete(ConfirmationToken token) {
        tokenRepository.delete(tokenMapper.toEntity(token));
    }

}
