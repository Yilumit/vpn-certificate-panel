package com.vpnpanel.VpnPanel.application.ports.out;

public interface VpnCertificateScriptPort {
    String createVpnCertificate(String userNickname);

    void revokeVpnCertificate(String userNickname, String file);

    byte[] readCertificateFile(String username, String filename);
}
