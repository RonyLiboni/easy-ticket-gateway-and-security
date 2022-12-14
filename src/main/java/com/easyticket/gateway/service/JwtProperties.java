package com.easyticket.gateway.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtProperties {
	private String secret;
}
