package keycloak.jsample.util;

import java.util.Arrays;
import java.util.List;

public class AppConstants {

	public static final class Uri {

		public static final String GET_USERS = "/admin/users";

		public static final String ADD_USER = "/admin/addUser";

		public static final String UPDATE_USER = "/admin/updateUser/{id}";

		public static final String DELETE_USER = "/admin/deleteUser/{id}";

	}

	public static final class Role {
		public static final String USER = "user";

		public static final String ADMIN = "admin";

		public static final List<String> APP_ROLES = Arrays.asList(USER, ADMIN);
	}

	public static final class Msg {

		public static final String ADD_USER = "Id of the created user %s";
		
		public static final String UPDATE_USER = "User with id %s is updated";

		public static final String DELETE_USER = "User with id %s is deleted";
	}
}
