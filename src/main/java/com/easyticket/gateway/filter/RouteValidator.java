package com.easyticket.gateway.filter;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("endpoints")
@Getter
@Setter
public class RouteValidator {
	
	private List<SecuredRoute> securedRoutes = List.of(new SecuredRoute("/admin/", "ROLE_ADMIN"), 
														new SecuredRoute("/user/", "ROLE_USER"));
	
	private List<String> permitAllRoutes = List.of("/swagger-ui/", 
										    		"/v3/api-docs/",
										    		"/all/");
        
	public boolean isUserPermissionInvalid(String path, String userRoles) {
		return securedRoutes.stream()
							.filter(route -> path.contains(route.getSecuredRoute()))
							.noneMatch(route -> userRoles.contains(route.getRequiredRole()));
	}
	
	public boolean isASecuredRoute(String path) {
		return permitAllRoutes.stream()
							.noneMatch(permitedRoute -> path.contains(permitedRoute));
	}
}