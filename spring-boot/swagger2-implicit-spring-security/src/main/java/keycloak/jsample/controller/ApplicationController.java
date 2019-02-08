package keycloak.jsample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import keycloak.jsample.api.AdminApi;
import keycloak.jsample.api.HelloApi;
import keycloak.jsample.util.AppConstants;

@RestController
public class ApplicationController implements AdminApi, HelloApi {

	@Override
	public ResponseEntity<String> getMsg() {
		return new ResponseEntity<String>(AppConstants.Response.HELLO, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<String> getAdminMsg() {
		return new ResponseEntity<String>(AppConstants.Response.ADMIN_HELLO, HttpStatus.OK);
	}
}
