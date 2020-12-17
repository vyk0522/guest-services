package com.onejava.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {
    private String SECRET_KEY;
    private long validityInMilliseconds;
    private final String ROLES_KEY = "roles";

    @Autowired
    public JwtUtil(@Value("${security.jwt.token.secret-key}") String secretKey,
                       @Value("${security.jwt.token.expiration}")long validityInMilliseconds) {

        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        String subject = userDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES_KEY, authorities);
        return createToken(claims, subject);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
