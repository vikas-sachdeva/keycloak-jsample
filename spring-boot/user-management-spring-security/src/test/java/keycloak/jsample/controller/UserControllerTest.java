package keycloak.jsample.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import keycloak.jsample.model.User;
import keycloak.jsample.services.UserService;
import keycloak.jsample.util.AppConstants;

@SpringJUnitWebConfig
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private static List<User> userList;

	@BeforeAll
	private static void init() {
		User user1 = new User("user-1", "user1", "user1", "user1", "user1@test.com", "password", "admin");
		User user2 = new User("user-2", "user2", "user2", "user2", "user2@test.com", "password", "admin");
		User user3 = new User("user-3", "user3", "user3", "user3", "user3@test.com", "password", "admin");

		userList = new ArrayList<>(Arrays.asList(user1, user2, user3));
	}

	@Test
	/*
	 * It does not matter whether this mock user exist or not. Request does not go
	 * to keycloak server while executing these test cases.
	 */
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.USER)
	public void listUsersTest1() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.Uri.GET_USERS);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.ADMIN)
	public void listUsersTest2() throws Exception {

		Mockito.when(userService.listUsers()).thenReturn(userList);

		ObjectMapper mapper = new ObjectMapper();
		String expectedResponse = mapper.writeValueAsString(userList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(AppConstants.Uri.GET_USERS);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));
	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.ADMIN)
	public void addUserTest1() throws Exception {

		String id = "newuserid-111";

		User user1 = new User(null, "user1", "user1", "user1", "user1@test.com", "password", "admin");
		ObjectMapper mapper = new ObjectMapper();
		String request = mapper.writeValueAsString(user1);

		Mockito.when(userService.addUser(user1)).thenReturn(id);

		String expectedResponse = String.format(AppConstants.Msg.ADD_USER, id);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(AppConstants.Uri.ADD_USER).content(request)
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedResponse));
	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.ADMIN)
	public void updateUserTest1() throws Exception {

		String id = "newuserid-111";

		User user1 = new User(id, "user1", "user1", "user1", "user1@test.com", "password", "admin");
		ObjectMapper mapper = new ObjectMapper();
		String request = mapper.writeValueAsString(user1);

		Mockito.doNothing().when(userService).updateUser(id, user1);

		String expectedResponse = String.format(AppConstants.Msg.UPDATE_USER, id);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(AppConstants.Uri.UPDATE_USER, id).content(request)
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedResponse));
	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.ADMIN)
	public void deleteUserTest1() throws Exception {

		String id = "newuserid-111";

		Mockito.doNothing().when(userService).deleteUser(id);

		String expectedResponse = String.format(AppConstants.Msg.DELETE_USER, id);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(AppConstants.Uri.DELETE_USER, id);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(expectedResponse));
	}

}