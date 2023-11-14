package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.EditUserDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

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

	@GetMapping(value = "/profile")
	public ResponseEntity<?> profile(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			ApiResponse<User> user = userService.profile(authorizationHeader);

			return ResponseEntity.ok().body(user);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@PostMapping(value = "/signout")
	public ResponseEntity<?> signout(@RequestBody CreateUserDto user) {
		try {			
			ApiTokenResponse<User> response = userService.signout(user);
			
			return ResponseEntity.ok().body(response);
		} catch (ExistUserException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
	    } catch (ResourceNotFoundException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
	    } catch (IllegalArgumentException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
	    }
	}

	@PostMapping(value = "/signin")
	public ResponseEntity<?> signin(@RequestBody SigninDto user) {
		try {
			ApiTokenResponse<User> response = userService.signin(user);

			return ResponseEntity.ok().body(response);
		} catch (ResourceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
		} catch (IllegalArgumentException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
		}
	}

	@PatchMapping(value = "/edit/{id}")
	public ResponseEntity<?> edit(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @ModelAttribute EditUserDto user, MultipartFile image) throws IOException, IllegalArgumentException, SQLException {
		// @ModelAttribute to accept multiform/form-data
		try {
			ApiResponse<User> response = userService.edit(authorizationHeader, id, user, image);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		} catch (IOException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
		}
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = userService.delete(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@PostMapping(value = "/fav/barbershop/{id}")
	public ResponseEntity<?> createRelationWithBarbershop(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = userService.createRelationWithBarbershop(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (IllegalArgumentException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
		}
	}

	@PostMapping(value = "/unfav/barbershop/{id}")
	public ResponseEntity<?> deleteRelationWithBarbershop(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = userService.deleteRelationWithBarbershop(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (IllegalArgumentException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
		}
	}
	
}
