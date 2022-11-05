package com.easyticket.gateway.filter;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("validator")
@Getter
@Setter
public class PathValidator {
	
	private List<SecuredPath> securedPaths = List.of(new SecuredPath("/admin/", "ROLE_ADMIN"), 
														new SecuredPath("/user/", "ROLE_USER"));
	
	private List<String> permitAllPaths = List.of("/swagger-ui/", 
										    		"/v3/api-docs/",
										    		"/all/");
        
	public boolean isUserPermissionInvalid(String path, String userRoles) {
		return securedPaths.stream()
							.filter(route -> path.contains(route.getSecuredPath()))
							.noneMatch(route -> userRoles.contains(route.getRequiredRole()));
	}
	
	public boolean isASecuredPath(String path) {
		return permitAllPaths.stream()
							.noneMatch(permitedRoute -> path.contains(permitedRoute));
	}
}