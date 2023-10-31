package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.CreateServiceDto;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/service")
public class ServiceResource {

    @Autowired
    private ServiceService serviceService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> create (@RequestHeader("Authorization") String authorizationHeader, @RequestBody CreateServiceDto service) {
        try {
            ApiResponse<Service> responseService = serviceService.create(authorizationHeader, service);

            return ResponseEntity.ok().body(responseService);
        } catch (ExistUserException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
        }
    }
}
