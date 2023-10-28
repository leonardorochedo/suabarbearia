package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.BarbershopService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/barbershop")
public class BarbershopResource {
	
	@Autowired
	private BarbershopService barbershopService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Barbershop> findById(@PathVariable Long id) {
		Barbershop barbershop = barbershopService.findById(id);
		
		return ResponseEntity.ok().body(barbershop);
	}
	
	@PostMapping(value = "/signout")
	public ResponseEntity<?> signout(@RequestBody CreateBarbershopDto barbershop) {
		try {			
			ApiTokenResponse<Barbershop> response = barbershopService.signout(barbershop);
			
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
	
	@PostMapping(value = "/signin")
	public ResponseEntity<?> signin(@RequestBody SigninDto barbershop) {
  		try {
	 		ApiTokenResponse<Barbershop> response = barbershopService.signin(barbershop);

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

}
