package com.vpnpanel.VpnPanel.application.ports;

public interface MailService {
    void send(String recipient, String subject, String message);

    String createFinalizationEmail(String name, String link);
}
