package keycloak.jsample.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakUri {

	@Value("${keycloak.auth-server-url}")
	private String keycloakUrl;

	@Value("${keycloak.realm}")
	private String realmName;

	private static final String ADMIN_URI_PREFIX = "/admin/realms/";

	public URI getUserUri() throws URISyntaxException {
		StringBuilder usersListUri = new StringBuilder(keycloakUrl);
		usersListUri.append(ADMIN_URI_PREFIX).append(realmName).append("/users/");
		return new URI(usersListUri.toString());
	}
	
	public URI getUserUriWithUserId(String userId) throws URISyntaxException {
		StringBuilder usersListUri = new StringBuilder(keycloakUrl);
		usersListUri.append(ADMIN_URI_PREFIX).append(realmName).append("/users/").append(userId);
		return new URI(usersListUri.toString());
	}

	public URI getAssignedRoleUriWithUserId(String userId) throws URISyntaxException {
		StringBuilder usersListUri = new StringBuilder(keycloakUrl);
		usersListUri.append(ADMIN_URI_PREFIX).append(realmName).append("/users/").append(userId)
				.append("/role-mappings/realm/");
		return new URI(usersListUri.toString());
	}
	
}
