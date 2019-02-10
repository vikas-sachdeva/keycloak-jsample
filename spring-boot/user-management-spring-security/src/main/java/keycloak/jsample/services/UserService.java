package keycloak.jsample.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import keycloak.jsample.model.User;
import keycloak.jsample.util.AppConstants;

@Service
public class UserService {

	@Autowired
	private KeycloakUserService keycloakUserService;

	@Autowired
	private KeycloakUtils keycloakUtils;

	public List<User> listUsers() throws URISyntaxException {
		List<User> userList = new ArrayList<>();
		List<UserRepresentation> userRepresentations = keycloakUserService.listUsers();

		for (UserRepresentation userRepresentation : userRepresentations) {
			User user = new User();
			userList.add(user);

			BeanUtils.copyProperties(userRepresentation, user);

			String role = getUserApplicationRole(userRepresentation.getId());

			user.setRole(role);
		}
		return userList;
	}

	private String getUserApplicationRole(String userId) throws URISyntaxException {
		List<RoleRepresentation> roleRepresentations = keycloakUserService.getAssignedRealmRoleByUserId(userId);

		String role = roleRepresentations.stream().filter(x -> AppConstants.Role.APP_ROLES.contains(x.getName()))
				.findFirst().map(x -> x.getName()).orElse(null);
		return role;
	}

	public String addUser(User user) throws URISyntaxException {

		UserRepresentation userRepresentation = new UserRepresentation();

		BeanUtils.copyProperties(user, userRepresentation);
		userRepresentation.setEnabled(true);

		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setValue(user.getPassword());
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

		userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));

		String id = keycloakUserService.addUser(userRepresentation);

		updateAssignedRole(id, user.getRole());

		return id;
	}

	private void updateAssignedRole(String id, String role) throws URISyntaxException {
		RoleRepresentation roleRepresentation = new RoleRepresentation();
		roleRepresentation.setName(role);
		roleRepresentation.setId(keycloakUtils.getRoleIdByRoleName(role));

		keycloakUserService.setAssignedRealmRoleByUserId(id, roleRepresentation);
	}

	public void updateUser(String userId, User user) throws URISyntaxException {

		UserRepresentation userRepresentation = new UserRepresentation();

		BeanUtils.copyProperties(user, userRepresentation);
		userRepresentation.setEnabled(true);
		userRepresentation.setId(userId);

		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setValue(user.getPassword());
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

		userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));

		keycloakUserService.updateUser(userId, userRepresentation);

		String currentRole = getUserApplicationRole(userRepresentation.getId());
		if (!currentRole.equals(user.getRole())) {

			RoleRepresentation roleRepresentation = new RoleRepresentation();
			roleRepresentation.setName(currentRole);
			roleRepresentation.setId(keycloakUtils.getRoleIdByRoleName(currentRole));
			keycloakUserService.deleteAssignedRealmRoleByUserId(userId, roleRepresentation);
			updateAssignedRole(userId, user.getRole());
		}
	}

	public void deleteUser(String userId) throws URISyntaxException {
		keycloakUserService.deleteUser(userId);
	}
}