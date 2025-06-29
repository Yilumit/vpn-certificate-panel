package com.vpnpanel.VpnPanel.adapters.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpnpanel.VpnPanel.application.ports.CertificateService;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    
    public List<Certificate> listCertificates(User user) {
        return certificateService.listCertificates(user);
    }

    // public ResponseEntity<Certificate> createCertificate(@AuthenticationPrincipal User user) {

    // }
}
