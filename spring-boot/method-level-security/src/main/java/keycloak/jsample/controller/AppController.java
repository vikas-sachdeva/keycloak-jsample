package keycloak.jsample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import keycloak.jsample.spring.AdminAccess;
import keycloak.jsample.spring.AllAccess;
import keycloak.jsample.util.AppConstants;

@RestController
public class AppController {

	@AllAccess
	@GetMapping(value = { AppConstants.Uri.GET_URL })
	public ResponseEntity<String> getMsg() {
		return new ResponseEntity<String>(AppConstants.Response.HELLO, HttpStatus.OK);
	}
	
	@AdminAccess
	@GetMapping(value = { AppConstants.Uri.ADMIN_URL })
	public ResponseEntity<String> getAdminMsg() {
		return new ResponseEntity<String>(AppConstants.Response.ADMIN_HELLO, HttpStatus.OK);
	}
}
