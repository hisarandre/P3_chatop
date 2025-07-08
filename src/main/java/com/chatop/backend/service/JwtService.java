package com.chatop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        try {
            Instant now = Instant.now();

            // Build token claims
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("chatop")
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .subject(authentication.getName())
                    .build();

            // Encodage
            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS256).build(),
                    claims
            );

            // Generate token
            String token = this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return token;

        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

}