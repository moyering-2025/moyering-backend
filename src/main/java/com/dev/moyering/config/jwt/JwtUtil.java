package com.dev.moyering.config.jwt;

import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String secretKey = "코스타";

    public Integer extractUserIdFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String token = header.substring("Bearer ".length());
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("id", Integer.class); // ← 토큰에 저장한 userId 꺼냄
        } catch (SignatureException | IllegalArgumentException e) {
            return null; // 유효하지 않은 토큰
        }
    }
}