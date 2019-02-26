package keycloak.jsample.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import keycloak.jsample.util.AppConstants;

@SpringJUnitWebConfig
@AutoConfigureMockMvc
@SpringBootTest
public class AppControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	/*
	 * It does not matter whether this mock user exist or not.
	 * Request does not go to keycloak server while executing these test cases.
	 */
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.USER)
	public void getMsgTest1() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(keycloak.jsample.util.AppConstants.Uri.GET_URL);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(AppConstants.Response.HELLO));
	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = AppConstants.Role.ADMIN)
	public void getAdminMsgTest() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(keycloak.jsample.util.AppConstants.Uri.ADMIN_URL);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(AppConstants.Response.ADMIN_HELLO));
	}
}