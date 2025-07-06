package com.vpnpanel.VpnPanel.adapters.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    @GetMapping
    public ResponseEntity<List<Certificate>> listCertificates(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok( certificateService.listCertificates(user) );
    }

    @PostMapping
    public ResponseEntity<Certificate> generateCertificate(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok( certificateService.createCertificate(user) );
    }

    @DeleteMapping("/{file}")
    public ResponseEntity<Certificate> revokeCertificate(@AuthenticationPrincipal User user, @PathVariable String file) {
        certificateService.revokeCertificate(user, file);
        return ResponseEntity.noContent().build();
    }

}
