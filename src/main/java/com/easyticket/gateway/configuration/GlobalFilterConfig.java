package com.easyticket.gateway.configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.easyticket.gateway.filter.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GlobalFilterConfig {
	
	private final AuthenticationService authenticationService;
	
	@Bean
	public GlobalFilter globalFilter() {
		return (exchange, chain) -> {		
			authenticationService.authenticate(exchange);			
			return chain.filter(exchange);
		};
	}
}
