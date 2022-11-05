package com.easyticket.gateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.easyticket.gateway.exception.ForbiddenException;
import com.easyticket.gateway.service.JwtTokenService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationService {

	private final PathValidator pathValidator;
	private final JwtTokenService jwtTokenService;
	
	public ServerWebExchange authenticate(ServerWebExchange exchange) {
		var request = exchange.getRequest();
		var path = request.getURI().getPath();
		
		if (pathValidator.isASecuredPath(path)) {
			final String token = jwtTokenService.getTokenOrThrowUnauthorizedException(request);
						
			if (pathValidator.isUserPermissionInvalid(path, jwtTokenService.getRolesFrom(token))) {
				throw new ForbiddenException("You don't have enough permissions to access this endpoint");	
			}
			populateRequestWithHeaders(exchange, token);
		}
		return exchange;
	}

	private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
		exchange.getRequest().mutate().header("id", jwtTokenService.getUserId(token));
	}

}
