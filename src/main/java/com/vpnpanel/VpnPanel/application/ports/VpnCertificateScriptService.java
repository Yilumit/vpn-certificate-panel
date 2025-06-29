package com.vpnpanel.VpnPanel.application.ports;


public interface VpnCertificateScriptService {
    String createVpnCertificate(String userNickname);

    void revokeVpnCertificate(String userNickname, String file);
    
}
