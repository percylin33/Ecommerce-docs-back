package com.carpetadigital.ecommerce.Auth.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final Set<String> invalidTokens = new HashSet<>();
    private static final String SECRET_KEY="53F8FS5613SDF2G3S1846121SD543EG14H5121421242SDD941J82";

    public String getToken(UserDetails user, String name, String lastname, String image) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", name);
        extraClaims.put("lastname", lastname);
        extraClaims.put("image", image);
        return getToken(extraClaims, user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 minutos de expiración
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    public void invalidateToken(String token) {
        invalidTokens.add(token);
    }

    public Map<String, Object> getUserInfoFromToken(String token) {
        Claims claims = getAllClaims(token);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", claims.getSubject());
        userInfo.put("name", claims.get("name"));
        userInfo.put("lastname", claims.get("lastname"));
        userInfo.put("image", claims.get("image"));
        return userInfo;
    }
}
