package com.vpnpanel.VpnPanel.adapters.out.security.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKeyEncoded;

    @Value("${security.jwt.token.expire-length}")
    private long validityMilliseconds;

    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyEncoded);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        String nickname = authentication.getName();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMilliseconds);

        // Gera uma string unica com todas as roles do usuario
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

                // Gera o web token
        return Jwts.builder()
                .setSubject(nickname)
                .claim("auth", authorities)
                .setIssuedAt(now).setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token); //verifica se o token e valido e foi corretamente assinado
        
        String authoritiesClaim = claims.get("auth", String.class);
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(authoritiesClaim.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
        
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        //Objeto sera inserido no contexto de seguranca do spring para indicar uma requisicao autenticada
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public long getExpirationMillis() {
    return validityMilliseconds;
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
}
