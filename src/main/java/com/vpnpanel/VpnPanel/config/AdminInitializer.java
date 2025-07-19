package com.vpnpanel.VpnPanel.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.RoleEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.UserEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.RoleRepository;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.UserRepository;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn("roleInitializer")
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.email}")
    private String defaultEmail;

    @Value("${admin.default.nickname}")
    private String defaultNickname;

    @Value("${admin.default.password}")
    private String defaultPassword;

    @PostConstruct
    public void initAdminUser() {
        if (!userRepository.existsByEmail(defaultEmail)) {
            RoleEntity adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Role ADMIN nao encontrada"));

            UserEntity admin = new UserEntity();
            admin.setName("Administrador Padrao");
            admin.setNickname(defaultNickname);
            admin.setEmail(defaultEmail);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setActive(true);
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            log.info("Usuario administrador padrao criado com sucesso.");
        } else {
            log.info("Usuario administrador padrao ja existe.");
        }
    }
}
