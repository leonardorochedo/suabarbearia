package com.suabarbearia.backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		User user = userService.findById(id);
		
		return ResponseEntity.ok().body(user);
	}
	
	@PostMapping(value = "/signout")
	public ResponseEntity<?> signout(@RequestBody CreateUserDto user) {
		try {			
			ApiTokenResponse<User> response = userService.signout(user);
			
			return ResponseEntity.ok().body(response);
		} catch (ExistUserException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
	    } catch (ResourceNotFoundException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
	    } catch (IllegalArgumentException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
	    }
	}
	
}
