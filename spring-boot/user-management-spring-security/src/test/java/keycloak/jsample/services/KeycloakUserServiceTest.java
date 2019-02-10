package keycloak.jsample.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.MultiValueMap;

import keycloak.jsample.model.User;
import keycloak.jsample.util.AppConstants;

@SpringJUnitWebConfig
@AutoConfigureMockMvc
@SpringBootTest
public class KeycloakUserServiceTest {

	@InjectMocks
	private KeycloakUserService keycloakUserService;

	@MockBean
	private KeycloakRestTemplate restTemplate;

	@SpyBean
	private KeycloakUri keycloakUri;

	private static List<UserRepresentation> userRepresentationList;

	private static List<RoleRepresentation> roleRepresentationList;

	private static List<User> userList;

	@BeforeAll
	private static void init() {

		UserRepresentation user1 = new UserRepresentation();
		user1.setId("user-1");
		user1.setFirstName("user1");
		user1.setLastName("user1");
		user1.setEmail("user1@test.com");
		user1.setUsername("user1");

		UserRepresentation user2 = new UserRepresentation();
		user2.setId("user-2");
		user2.setFirstName("user2");
		user2.setLastName("user2");
		user2.setEmail("user2@test.com");
		user2.setUsername("user2");

		userRepresentationList = new ArrayList<>(Arrays.asList(user1, user2));

		RoleRepresentation roleRepresentation1 = new RoleRepresentation();
		roleRepresentation1.setId("role-1");
		roleRepresentation1.setName(AppConstants.Role.ADMIN);

		RoleRepresentation roleRepresentation2 = new RoleRepresentation();
		roleRepresentation2.setId("role-2");
		roleRepresentation2.setName(AppConstants.Role.ADMIN);

		roleRepresentationList = new ArrayList<RoleRepresentation>(
				Arrays.asList(roleRepresentation1, roleRepresentation2));

		userList = new ArrayList<>();

		for (int i = 0; i < userRepresentationList.size(); i++) {

			User user = new User();
			BeanUtils.copyProperties(userRepresentationList.get(i), user);
			user.setRole(roleRepresentationList.get(i).getName());
			userList.add(user);
		}
	}

	@BeforeEach
	private void initTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void listUsersTest1() throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.GET, keycloakUri.getUserUri());

		ResponseEntity<List<UserRepresentation>> expectedResponse = new ResponseEntity<List<UserRepresentation>>(
				userRepresentationList, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, new ParameterizedTypeReference<List<UserRepresentation>>() {
		})).thenReturn(expectedResponse);

		List<UserRepresentation> actualResponse = keycloakUserService.listUsers();

		Assertions.assertArrayEquals(userRepresentationList.toArray(), actualResponse.toArray());

	}

	@Test
	public void getAssignedRealmRoleByUserIdTest1() throws URISyntaxException {

		String userId = "user-id-302";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.GET,
				keycloakUri.getAssignedRoleUriWithUserId(userId));

		ResponseEntity<List<RoleRepresentation>> expectedResponse = new ResponseEntity<List<RoleRepresentation>>(
				roleRepresentationList, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, new ParameterizedTypeReference<List<RoleRepresentation>>() {
		})).thenReturn(expectedResponse);

		List<RoleRepresentation> actualResponse = keycloakUserService.getAssignedRealmRoleByUserId(userId);

		Assertions.assertArrayEquals(roleRepresentationList.toArray(), actualResponse.toArray());
	}

	@Test
	public void addUserTest1() throws URISyntaxException {

		String userId = "user-id-302";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<UserRepresentation> entity = new RequestEntity<UserRepresentation>(userRepresentationList.get(0),
				headers, HttpMethod.POST, keycloakUri.getUserUri());

		MultiValueMap<String, String> responseHeaders = new HttpHeaders();

		String locationHeaderValue = keycloakUri.getUserUri().toString() + userId;

		responseHeaders.put(HttpHeaders.LOCATION, Arrays.asList(locationHeaderValue));

		ResponseEntity<String> expectedResponse = new ResponseEntity<String>(responseHeaders, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, String.class)).thenReturn(expectedResponse);

		String actualResponse = keycloakUserService.addUser(userRepresentationList.get(0));

		Assertions.assertEquals(userId, actualResponse);
	}

	@Test
	public void setAssignedRealmRoleByUserIdTest1() throws URISyntaxException {

		String userId = "user-id-302";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<List<RoleRepresentation>> entity = new RequestEntity<List<RoleRepresentation>>(headers,
				HttpMethod.POST, keycloakUri.getAssignedRoleUriWithUserId(userId));

		ResponseEntity<String> expectedResponse = new ResponseEntity<String>(HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, String.class)).thenReturn(expectedResponse);

		Assertions.assertDoesNotThrow(
				() -> keycloakUserService.setAssignedRealmRoleByUserId(userId, roleRepresentationList.get(0)));

	}

	@Test
	public void updateUserTest1() throws URISyntaxException {

		String userId = "user-id-302";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<UserRepresentation> entity = new RequestEntity<UserRepresentation>(userRepresentationList.get(0),
				headers, HttpMethod.PUT, keycloakUri.getUserUriWithUserId(userId));

		ResponseEntity<String> expectedResponse = new ResponseEntity<String>(HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, String.class)).thenReturn(expectedResponse);

		Assertions.assertDoesNotThrow(() -> keycloakUserService.updateUser(userId, userRepresentationList.get(0)));

	}

	@Test
	public void deleteAssignedRealmRoleByUserIdTest1() throws URISyntaxException {

		String userId = "user-id-302";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<List<RoleRepresentation>> entity = new RequestEntity<List<RoleRepresentation>>(
				Arrays.asList(roleRepresentationList.get(0)), headers, HttpMethod.DELETE,
				keycloakUri.getAssignedRoleUriWithUserId(userId));

		ResponseEntity<String> expectedResponse = new ResponseEntity<String>(HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, String.class)).thenReturn(expectedResponse);

		Assertions.assertDoesNotThrow(
				() -> keycloakUserService.deleteAssignedRealmRoleByUserId(userId, roleRepresentationList.get(0)));

	}

	@Test
	public void deleteUserTest1() throws URISyntaxException {

		String userId = "user-id-302";
		HttpHeaders headers = new HttpHeaders();

		RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.DELETE,
				keycloakUri.getUserUriWithUserId(userId));

		ResponseEntity<String> expectedResponse = new ResponseEntity<String>(HttpStatus.OK);

		Mockito.when(restTemplate.exchange(entity, String.class)).thenReturn(expectedResponse);

		Assertions.assertDoesNotThrow(() -> keycloakUserService.deleteUser(userId));

	}
}
