package com.vpnpanel.VpnPanel.application.ports.out;

public interface MailPort {
    void send(String recipient, String subject, String message);

    String createFinalizationEmail(String name, String link);
    String createResetEmail(String name, String link);
}

