package com.suabarbearia.backend.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import com.suabarbearia.backend.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generated key in HS256 pattern
    private static final long EXPIRATION_TIME = 86400000; // 24hrs in miliseconds

    public static String generateToken(String username) {
    	// Expiration time
    	Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String generateTokenWhenForgotPassword(String username) {
        // Expiration time
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000); // 1 hour

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEmailFromToken(String token) {
    	Claims claims = Jwts.parserBuilder()
    			.setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    	
        return claims.getSubject();
    }
    

 	public static  String verifyTokenWithAuthorizationHeader(String authorizationHeader) {
 		String token = authorizationHeader.replace("Bearer ", "");
         
 		boolean isValidToken = JwtUtil.validateToken(token);

         if (!isValidToken) {
             throw new InvalidTokenException("Token inválido!");
         }
         
         return token;
 	}

}
