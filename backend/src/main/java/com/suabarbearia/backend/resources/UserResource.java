package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.EditUserDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

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

	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup(@RequestBody CreateUserDto user) {
		try {			
			ApiTokenResponse<User> response = userService.signup(user);
			
			return ResponseEntity.ok().body(response);
		} catch (ExistDataException e) {
	        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
	        
	        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
	    } catch (PasswordDontMatchException | FieldsAreNullException e) {
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
		} catch (InvalidDataException | FieldsAreNullException e) {
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
		} catch (InvalidTokenException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		} catch (FieldsAreNullException | PasswordDontMatchException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
		}
	}

	@PostMapping(value = "/sendemailpassword/{email}")
	public ResponseEntity<?> sendEmailPassword(@PathVariable String email) {
		try {
			TextResponse response = userService.sendEmailPassword(email);

			return ResponseEntity.ok().body(response);
		} catch (MailException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorResponse);
		} catch (ResourceNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
		}
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = userService.delete(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (InvalidTokenException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@PostMapping(value = "/fav/barbershop/{id}")
	public ResponseEntity<?> createRelationWithBarbershop(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = userService.createRelationWithBarbershop(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (AlreadySelectedDataException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
		}
	}

	@PostMapping(value = "/unfav/barbershop/{id}")
	public ResponseEntity<?> deleteRelationWithBarbershop(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = userService.deleteRelationWithBarbershop(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (DisabledDataException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
		}
	}

	@GetMapping(value = "/schedulings")
	public ResponseEntity<?> getSchedulingsUser(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			Set<Scheduling> response = userService.getSchedulingsUser(authorizationHeader);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/schedulings/{initialDate}/{endDate}")
	public ResponseEntity<?> getSchedulingsUserWithDate(@RequestHeader("Authorization") String authorizationHeader, @PathVariable LocalDate initialDate, @PathVariable LocalDate endDate) {
		try {
			Set<Scheduling> response = userService.getSchedulingsUserWithDate(authorizationHeader, initialDate, endDate);

			return ResponseEntity.ok().body(response);
		} catch (InvalidDataException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
		}
	}

	@GetMapping(value = "/barbershops")
	public ResponseEntity<?> getBarbershopsUser(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			Set<Barbershop> response = userService.getBarbershopsUser(authorizationHeader);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

}
