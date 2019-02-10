package keycloak.jsample.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import keycloak.jsample.model.User;
import keycloak.jsample.services.UserService;
import keycloak.jsample.util.AppConstants;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = { AppConstants.Uri.GET_USERS })
	public ResponseEntity<List<User>> listUsers() throws URISyntaxException {
		return new ResponseEntity<List<User>>(userService.listUsers(), HttpStatus.OK);
	}

	@PostMapping(value = { AppConstants.Uri.ADD_USER })
	public ResponseEntity<String> addUser(@RequestBody User user) throws URISyntaxException {
		String msg = String.format(AppConstants.Msg.ADD_USER, userService.addUser(user));
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	@PutMapping(value = { AppConstants.Uri.UPDATE_USER })
	public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User user)
			throws URISyntaxException {
		userService.updateUser(id, user);
		String msg = String.format(AppConstants.Msg.UPDATE_USER, id);
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	@DeleteMapping(value = { AppConstants.Uri.DELETE_USER })
	public ResponseEntity<String> deleteUser(@PathVariable String id) throws URISyntaxException {
		userService.deleteUser(id);
		String msg = String.format(AppConstants.Msg.DELETE_USER, id);
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
}
