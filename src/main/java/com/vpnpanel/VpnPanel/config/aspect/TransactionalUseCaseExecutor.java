package com.vpnpanel.VpnPanel.config.aspect;

import java.util.function.Supplier;

import jakarta.transaction.Transactional;

public class TransactionalUseCaseExecutor {
    @Transactional
    <T> T executeInTransaction(Supplier<T> execution) {
        return execution.get();
    }

}
