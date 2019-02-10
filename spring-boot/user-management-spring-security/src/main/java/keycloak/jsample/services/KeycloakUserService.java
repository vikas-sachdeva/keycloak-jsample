package keycloak.jsample.services;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KeycloakUserService {

	@Autowired
	private KeycloakUri keycloakUri;

	@Autowired
	private KeycloakRestTemplate restTemplate;

	public List<UserRepresentation> listUsers() throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.GET, keycloakUri.getUserUri());

		ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(entity,
				new ParameterizedTypeReference<List<UserRepresentation>>() {
				});

		return response.getBody();
	}

	public List<RoleRepresentation> getAssignedRealmRoleByUserId(String userId) throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.GET,
				keycloakUri.getAssignedRoleUriWithUserId(userId));

		ResponseEntity<List<RoleRepresentation>> response = restTemplate.exchange(entity,
				new ParameterizedTypeReference<List<RoleRepresentation>>() {
				});

		return response.getBody();
	}

	public String addUser(UserRepresentation userRepresentation) throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<UserRepresentation> entity = new RequestEntity<UserRepresentation>(userRepresentation, headers,
				HttpMethod.POST, keycloakUri.getUserUri());

		ResponseEntity<String> response = restTemplate.exchange(entity, String.class);

		return response.getHeaders().get(HttpHeaders.LOCATION).get(0).replace(keycloakUri.getUserUri().toString(), "");

	}

	public void setAssignedRealmRoleByUserId(String userId, RoleRepresentation roleRepresentation)
			throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<List<RoleRepresentation>> entity = new RequestEntity<List<RoleRepresentation>>(
				Arrays.asList(roleRepresentation), headers, HttpMethod.POST,
				keycloakUri.getAssignedRoleUriWithUserId(userId));

		restTemplate.exchange(entity, String.class);

	}

	public void updateUser(String userId, UserRepresentation userRepresentation) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<UserRepresentation> entity = new RequestEntity<UserRepresentation>(userRepresentation, headers,
				HttpMethod.PUT, keycloakUri.getUserUriWithUserId(userId));

		restTemplate.exchange(entity, String.class);

	}

	public void deleteAssignedRealmRoleByUserId(String userId, RoleRepresentation roleRepresentation)
			throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<List<RoleRepresentation>> entity = new RequestEntity<List<RoleRepresentation>>(
				Arrays.asList(roleRepresentation), headers, HttpMethod.DELETE,
				keycloakUri.getAssignedRoleUriWithUserId(userId));

		restTemplate.exchange(entity, String.class);

	}

	public void deleteUser(String userId) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();

		RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.DELETE,
				keycloakUri.getUserUriWithUserId(userId));

		restTemplate.exchange(entity, String.class);

	}
}