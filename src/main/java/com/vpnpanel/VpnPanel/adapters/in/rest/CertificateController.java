package com.vpnpanel.VpnPanel.adapters.in.rest;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vpnpanel.VpnPanel.application.ports.in.CertificateUseCase;
import com.vpnpanel.VpnPanel.domain.enums.CertificateStatus;
import com.vpnpanel.VpnPanel.domain.models.Certificate;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateUseCase certificateService;

    @GetMapping
    public ResponseEntity<List<Certificate>> listCertificates(@AuthenticationPrincipal User user,
            @RequestParam(required = false) CertificateStatus status) {
        List<Certificate> certificates = (status != null)
                ? certificateService.listByStatus(user, status)
                : certificateService.listCertificates(user);
                
        return ResponseEntity.ok(certificates);
    }

    @PostMapping
    public ResponseEntity<Certificate> createCertificate(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(certificateService.createCertificate(user));
    }

    @GetMapping("/download/{file}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String file,
            @AuthenticationPrincipal User user) {
        byte[] zipData = certificateService.downloadCertificate(user, file);
        ByteArrayResource resource = new ByteArrayResource(zipData);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file + ".zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipData.length).body(resource);
    }

    @DeleteMapping("/{file}")
    public ResponseEntity<Void> revokeCertificate(@AuthenticationPrincipal User user,
            @PathVariable String file) {
        certificateService.revokeCertificate(user, file);
        return ResponseEntity.noContent().build();
    }
    

}
