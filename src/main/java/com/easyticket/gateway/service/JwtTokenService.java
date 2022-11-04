package com.easyticket.gateway.service;

import java.util.Date;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import com.easyticket.gateway.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final JwtProperties jwtProperties;

	public Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new UnauthorizedException("Provided token is invalid!");
		}
	}

	private boolean isTokenExpired(String token) {
		return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
	}

	public String getRolesFrom(String token) {
		return getAllClaimsFromToken(token).getAudience();
	}

	public String getTokenOrThrowUnauthorizedException(ServerHttpRequest request) {
		if (request.getHeaders().containsKey("Authorization")) {
			return throwExceptionIfTokenIsMissing(request.getHeaders().getOrEmpty("Authorization").get(0));
		}
		throw new UnauthorizedException("Authentication header is missing!");
	}

	private String throwExceptionIfTokenIsMissing(String token) {
		if (token == null || token.length() < 7) {
			throw new UnauthorizedException("Token is missing or is invalid!");
		}
		return throwExceptionIfTokenIsInvalid(token.substring(7)); // get token and removes "Bearer " from it
	}

	private String throwExceptionIfTokenIsInvalid(String token) {
		if (isTokenExpired(token)) {
			throw new UnauthorizedException("Provided token is invalid!");
		}
		return token;
	}

	public String getUserId(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}
}
