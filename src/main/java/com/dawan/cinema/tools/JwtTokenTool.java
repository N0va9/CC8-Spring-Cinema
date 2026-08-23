package com.dawan.cinema.tools;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenTool {
	 // durée de validité du token
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; // 1 jours - en secondes

    @Value("${jwt.secret}")
    private String secret;
    
    public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public static long getJwtTokenValidity() {
		return JWT_TOKEN_VALIDITY;
	}

	public String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
        		.claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getKey()).compact();

    }
    
    public SecretKey getKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
        
    }
}