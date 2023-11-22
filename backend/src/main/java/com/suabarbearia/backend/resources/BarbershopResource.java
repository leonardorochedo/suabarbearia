package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.BarbershopService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping(value = "/barbershops")
public class BarbershopResource {
	
	@Autowired
	private BarbershopService barbershopService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Barbershop> findById(@PathVariable Long id) {
		Barbershop barbershop = barbershopService.findById(id);
		
		return ResponseEntity.ok().body(barbershop);
	}

	@GetMapping(value = "/profile")
	public ResponseEntity<?> profile(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			ApiResponse<Barbershop> barbershop = barbershopService.profile(authorizationHeader);

			return ResponseEntity.ok().body(barbershop);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}
	
	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup(@RequestBody CreateBarbershopDto barbershop) {
		try {			
			ApiTokenResponse<Barbershop> response = barbershopService.signup(barbershop);
			
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
	public ResponseEntity<?> signin(@RequestBody SigninDto barbershop) {
  		try {
	 		ApiTokenResponse<Barbershop> response = barbershopService.signin(barbershop);

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

	@PatchMapping(value = "/edit/{id}")
	public ResponseEntity<?> edit(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @ModelAttribute EditBarbershopDto barbershop, MultipartFile image) throws IOException, IllegalArgumentException, SQLException {
		// @ModelAttribute to accept multiform/form-data
		try {
			ApiResponse<Barbershop> response = barbershopService.edit(authorizationHeader, id, barbershop, image);

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
			TextResponse response = barbershopService.delete(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/users")
	public ResponseEntity<?> getUsersBarbershop(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			Set<User> response = barbershopService.getUsersBarbershop(authorizationHeader);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/employees")
	public ResponseEntity<?> getEmployeesBarbershop(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			Set<Employee> response = barbershopService.getEmployeesBarbershop(authorizationHeader);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/services")
	public ResponseEntity<?> getServicesBarbershop(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			Set<Service> response = barbershopService.getServicesBarbershop(authorizationHeader);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/schedulings")
	public ResponseEntity<?> getSchedulingsBarbershop(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			Set<Scheduling> response = barbershopService.getSchedulingsBarbershop(authorizationHeader);

			return ResponseEntity.ok().body(response);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/schedulings/{initialDate}/{endDate}")
	public ResponseEntity<?> getSchedulingsBarbershopWithDate(@RequestHeader("Authorization") String authorizationHeader, @PathVariable LocalDate initialDate, @PathVariable LocalDate endDate) {
		try {
			Set<Scheduling> response = barbershopService.getSchedulingsBarbershopWithDate(authorizationHeader, initialDate, endDate);

			return ResponseEntity.ok().body(response);
		} catch (IllegalArgumentException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		}
	}

	@GetMapping(value = "/schedulings/done/{id}")
	public ResponseEntity<?> concludeScheduling(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
		try {
			TextResponse response = barbershopService.concludeScheduling(authorizationHeader, id);

			return ResponseEntity.ok().body(response);
		} catch (IllegalArgumentException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
		} catch (RuntimeException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
		}
	}

}
