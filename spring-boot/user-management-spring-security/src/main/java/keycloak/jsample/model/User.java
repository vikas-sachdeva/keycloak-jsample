package keycloak.jsample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

}
