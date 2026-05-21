package com.abhiraj.PopcornPing.jwt;

import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.domain.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.expiry}")
    Long expirationTime;

    @Value("${jwt.secret.key}")
    String secretKey;

    public JwtResponseDto generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        String token =  Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getKey())
                .compact();
        return JwtResponseDto.builder()
                .token(token)
                .build();
    }

    public Key getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
