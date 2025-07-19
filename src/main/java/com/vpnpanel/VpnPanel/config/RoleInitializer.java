package com.vpnpanel.VpnPanel.config;

import org.springframework.stereotype.Component;

import com.vpnpanel.VpnPanel.adapters.out.jpa.entities.RoleEntity;
import com.vpnpanel.VpnPanel.adapters.out.jpa.repositories.RoleRepository;
import com.vpnpanel.VpnPanel.domain.enums.RoleType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("roleInitializer")
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        log.info("Verificando a existencia dos papeis no banco de dados");

        for (RoleType roleType : RoleType.values()) {
            roleRepository.findByName(roleType).ifPresentOrElse(role -> log.info("Papel {} ja existe.", roleType),
                    () -> {
                        RoleEntity newRole = new RoleEntity();
                        newRole.setName(roleType);
                        roleRepository.save(newRole);
                        log.info("Papel {} criado com sucesso.", roleType);
                    });
        }

        log.info("Verificacao de papeis finalizada");
    }
}
