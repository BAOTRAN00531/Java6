package com.example.assigment_java6.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;
    public SecurityUtil(JwtEncoder jwtEncoder)
    {
        this.jwtEncoder = jwtEncoder;
    }
    // Algorithm definition for hashing user password with HS512
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    // Config proxy variable for key of jwt
    @Value("${assigment_java6.jwt.base64-secret}")
    private String jwtKeyPath;
    // Config proxy variable for expiration of jwt
    @Value("${assigment_java6.jwt.token-validity-in-seconds}")
    private long jwtExpiryInSeconds;



    // Create a token to handle flow jwt
    public String createToken( Authentication authentication) {
        Instant now = Instant.now();
        Instant validity=now.plus(this.jwtExpiryInSeconds, ChronoUnit.SECONDS);
        //@formatter:off
        JwtClaimsSet claims=JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("Tran",authentication).build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return  this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claims)).getTokenValue();

    }
}
