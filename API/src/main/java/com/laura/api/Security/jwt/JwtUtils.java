package com.laura.api.Security.jwt;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

	private byte[] secret = Base64.getDecoder().decode("FzrjEf54HygsZxHaHqmPBGfvilRIET2e5srD70iLLTs=");

	public String generateJwtToken(String email) {

		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + 86400000))
				.signWith(Keys.hmacShaKeyFor(secret))
				.compact();
	}

	public String getEmail(String token) {
		Jws<Claims> jws;
		try {
			jws = Jwts.parserBuilder() 
				    .setSigningKey(Keys.hmacShaKeyFor(secret))
				    .build()                    
				    .parseClaimsJws(token);
			
			return jws.getBody().getSubject();
		}catch(JwtException ex) {
			return "";
		}
	}

	public boolean validateJwtToken(String token) {
		Jws<Claims> jws;
		try {
		    jws = Jwts.parserBuilder() 
		    .setSigningKey(Keys.hmacShaKeyFor(secret))
		    .build()                    
		    .parseClaimsJws(token); 
		
			return true;
			
		} catch (JwtException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
