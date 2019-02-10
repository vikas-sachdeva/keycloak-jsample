package keycloak.jsample.services;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import keycloak.jsample.util.AppConstants;

@Service
public class KeycloakUtils {

	@Value("${role.keycloak.id.admin}")
	private String adminRoleId;

	@Value("${role.keycloak.id.user}")
	private String userRoleId;

	private Map<String, String> roleMap = new LinkedHashMap<>();

	@PostConstruct
	private void createMap() {
		roleMap.clear();
		roleMap.put(AppConstants.Role.USER, userRoleId);
		roleMap.put(AppConstants.Role.ADMIN, adminRoleId);
	}

	public String getRoleIdByRoleName(String roleName) {
		return roleMap.get(roleName);
	}
}
