package com.easyticket.gateway.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SecuredRoute {
	private String securedRoute;
	private String requiredRole;
}
