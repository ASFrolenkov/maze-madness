package ru.mazemadness.maze_madness.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    private Key getSigningKey(){
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getVerifyKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//        return Jwts.SIG.HS512.key().build();
    }

    public String generateJwtToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", rolesList);
        claims.put("username", userDetails.getUsername());

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
//                .signWith(getSigningKey())
                .signWith(getSigningKey())
                .compact();
    }

    public String getJwtUsername(String token){
        return getAllClaimsFromJwtToken(token).getSubject();
    }

    public List<String> getJwtRoles(String token){
        return getAllClaimsFromJwtToken(token).get("roles", List.class);
    }

//    private Claims getAllClaimsFromJwtToken(String token){
//        return Jwts.parser()
//                .verifyWith(getVerifyKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }

    private Claims getAllClaimsFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith(getVerifyKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
