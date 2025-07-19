package com.vpnpanel.VpnPanel.adapters.in.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpnpanel.VpnPanel.adapters.in.rest.dto.UserDTO;
import com.vpnpanel.VpnPanel.application.ports.in.UserUseCase;
import com.vpnpanel.VpnPanel.domain.models.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserUseCase userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listAllUsers() {
        return ResponseEntity.ok(userService.listAllUsers()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/{nickname}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable String nickname) {
        User user = userService.deactivateUser(nickname);

        return ResponseEntity.ok(new UserDTO(user));
    }

    @PatchMapping("/{nickname}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> activateUser(@PathVariable String nickname) {
        User user = userService.activateUser(nickname);

        return ResponseEntity.ok(new UserDTO(user));
    }

    @PatchMapping("/{nickname}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> promoteToAdmin(@PathVariable String nickname) {
        User user = userService.promoteToAdmin(nickname);

        return ResponseEntity.ok(new UserDTO(user));
    }

    @PatchMapping("/{nickname}/demote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> demoteFromAdmin(@PathVariable String nickname) {
        User user = userService.demoteFromAdmin(nickname);

        return ResponseEntity.ok(new UserDTO(user));
    }

    @DeleteMapping("/{nickname}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String nickname) {
        userService.deleteUser(nickname);

        return ResponseEntity.noContent().build();
    }
}
