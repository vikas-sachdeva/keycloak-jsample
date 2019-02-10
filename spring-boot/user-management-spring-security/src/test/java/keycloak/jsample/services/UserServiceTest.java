package keycloak.jsample.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import keycloak.jsample.model.User;
import keycloak.jsample.util.AppConstants;

@SpringJUnitWebConfig
@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@MockBean
	private KeycloakUserService keycloakUserService;

	@SpyBean
	private KeycloakUtils keycloakUtils;

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

		Mockito.when(keycloakUserService.listUsers()).thenReturn(userRepresentationList);

		for (int i = 0; i < userRepresentationList.size(); i++) {

			Mockito.when(keycloakUserService.getAssignedRealmRoleByUserId(userRepresentationList.get(i).getId()))
					.thenReturn(Arrays.asList(roleRepresentationList.get(i)));
		}

		List<User> users = userService.listUsers();

		Assertions.assertArrayEquals(userList.toArray(), users.toArray());

	}

	@Test
	public void addUserTest1() throws URISyntaxException {

		String userId = "new-user-id";

		Mockito.when(keycloakUserService.addUser(Mockito.any())).thenReturn(userId);

		Mockito.doNothing().when(keycloakUserService).setAssignedRealmRoleByUserId(userId,
				roleRepresentationList.get(0));

		String id = userService.addUser(userList.get(0));

		Assertions.assertEquals(userId, id);

	}

	@Test
	public void updateUserTest1() throws URISyntaxException {

		String userId = "update-user-id";

		Mockito.doNothing().when(keycloakUserService).updateUser(Mockito.anyString(), Mockito.any());

		Mockito.when(keycloakUserService.getAssignedRealmRoleByUserId(userId)).thenReturn(roleRepresentationList);

		Mockito.doNothing().when(keycloakUserService).deleteAssignedRealmRoleByUserId(userId,
				roleRepresentationList.get(0));

		Mockito.doNothing().when(keycloakUserService).setAssignedRealmRoleByUserId(userId,
				roleRepresentationList.get(0));

		User user1 = new User("user-1", "user1", "user1", "user1", "user1@test.com", "password", "user");

		Assertions.assertDoesNotThrow(() -> userService.updateUser(userId, user1));

	}

	@Test
	public void deleteUserTest1() throws URISyntaxException {
		String userId = "delete-user-id";

		Mockito.doNothing().when(keycloakUserService).deleteUser(userId);
		
		Assertions.assertDoesNotThrow(() -> userService.deleteUser(userId));
	}
}
