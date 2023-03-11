package com.xmg.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author makui
 * @created 2023/3/10
 **/
@Slf4j
public class JwtUtil {

    public static final long TOKEN_VALIDITY_IN_MILLISECONDS = 3600 * 1000;
    private static final String EXPIRATION_TIME = "expirationTime"; //过期时间
    private static final String AUTHORITIES_KEY = "auth";
    private static final String CLAIM_KEY_USERNAME = "sub";

    private static final Key KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode("ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI"));

    public static String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        final Date validity = new Date(System.currentTimeMillis() + JwtUtil.TOKEN_VALIDITY_IN_MILLISECONDS);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, authentication.getName());
        claims.put(EXPIRATION_TIME, validity);
        claims.put(AUTHORITIES_KEY, authorities);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setExpiration(validity)
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public static Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public static boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(KEY).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e.getMessage(), e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e.getMessage(), e);
        }
        return false;
    }
}
