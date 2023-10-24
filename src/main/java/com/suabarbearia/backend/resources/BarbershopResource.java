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

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.BarbershopService;

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

}
