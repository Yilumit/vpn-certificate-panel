package com.vpnpanel.VpnPanel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.vpnpanel.VpnPanel.config.aspect.TransactionalUseCaseAspect;
import com.vpnpanel.VpnPanel.config.aspect.TransactionalUseCaseExecutor;

@Configuration
@ComponentScan(
    basePackages = "com.vpnpanel.VpnPanel.application",
    includeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION, value = UseCase.class
    )
)
public class DomainConfig {
        @Bean
    public TransactionalUseCaseAspect transactionalUseCaseAspect(
        TransactionalUseCaseExecutor transactionalUseCaseExecutor
    ) {
        return new TransactionalUseCaseAspect(transactionalUseCaseExecutor);
    }

    @Bean
    public TransactionalUseCaseExecutor transactionalUseCaseExecutor() {
        return new TransactionalUseCaseExecutor();
    }
}
