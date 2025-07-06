package com.vpnpanel.VpnPanel.adapters.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vpnpanel.VpnPanel.domain.models.User;
import com.vpnpanel.VpnPanel.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não encontrado!"));

        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getNickname())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(!user.isActive())
                .disabled(!user.isActive())
                .build();
    }

}
