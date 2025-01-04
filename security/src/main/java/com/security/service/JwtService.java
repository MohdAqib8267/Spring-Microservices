package com.security.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
	
	private String SECRET = "JwtImplementationjknjk2bckebckbekcbhdchkJwtImplementationjknjk2bckebckbekcbhdchkJwtImplementationjknjk2bckebckbekcbhdchk";
	
	public String generateToken(String username,String role) {
		Map<String,Object>claims = new HashMap<>();
		claims.put("role", role); // Add role to claims
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 5*60*60*1000))
				.and()
				.signWith(getKey())
				.compact();
	}
	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	public Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
//	Function<Claims, T> is a functional interface where:
//		Claims is the input type.
//		T is the output type.
	public <T> T extractClaim(String token,Function<Claims,T>claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	public String extractRole(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("role",String.class);
	}
	public boolean isTokenExpired(String token) {
		return extractClaim(token,Claims::getExpiration).before(new Date(System.currentTimeMillis()));
	}
	public boolean validateToken(String token,UserDetails userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && isTokenExpired(token)==false);
	}
	
}
