package com.ssafy.security;

import java.util.*;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration_time}")
	private long expirationTime;

	private final SecretKey key = Jwts.SIG.HS256.key().build();

	// JWT 생성
	public String generateToken(String memberId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("mid", memberId);

		// 필요한 경우 claims에 추가적인 정보 설정
		return Jwts.builder()
				.claims(claims)
				.subject(memberId)
				.signWith(key) // 서명 JWS
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expirationTime))
				.compact();
	}

//	// 토큰에서 사용자 ID 추출
//	public String getUserIdFromToken(String token) {
//		return Jwts.parser()
//                .verifyWith(key)
//				.build()
//				.parseSignedClaims(token)
//				.toString();
//	}

	public String getMemberIdFromToken(String token) {
		Jws<Claims> jws = Jwts.
				parser().
				verifyWith(key).
				build().
				parseSignedClaims(token);

		Claims claims = jws.getPayload();

        return claims.getSubject();
	}

//	public Authentication getAuthentication(String token){
//		Jws<Claims> jws = Jwts
//				.parser()
//				.verifyWith(key)
//				.build()
//				.parseSignedClaims(token);
//
//		Claims claims = jws.getPayload();
//		String memberId = claims.getSubject();
//
//        return new UsernamePasswordAuthenticationToken(
//				memberId,
//				null
////				Collections.singletonList() 권한 설정시
//		);
//	}

	// 토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


}