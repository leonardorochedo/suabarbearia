//package com.suabarbearia.backend.resources;
//
//import com.suabarbearia.backend.dtos.ServiceDto;
//import com.suabarbearia.backend.entities.Service;
//import com.suabarbearia.backend.exceptions.*;
//import com.suabarbearia.backend.responses.ApiResponse;
//import com.suabarbearia.backend.responses.ErrorResponse;
//import com.suabarbearia.backend.responses.TextResponse;
//import com.suabarbearia.backend.services.ServiceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(value = "/services")
//public class ServiceResource {
//
//    @Autowired
//    private ServiceService serviceService;
//
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<Service> findById(@PathVariable Long id) {
//        Service service = serviceService.findById(id);
//
//        return ResponseEntity.ok().body(service);
//    }
//
//    @PostMapping(value = "/create")
//    public ResponseEntity<?> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ServiceDto service) {
//        try {
//            ApiResponse<Service> responseService = serviceService.create(authorizationHeader, service);
//
//            return ResponseEntity.ok().body(responseService);
//        } catch (ExistDataException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
//        } catch (FieldsAreNullException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
//        }
//    }
//
//    @PatchMapping(value = "/edit/{id}")
//    public ResponseEntity<?> edit(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody ServiceDto service) {
//        try {
//            ApiResponse<Service> responseService = serviceService.edit(authorizationHeader, id, service);
//
//            return ResponseEntity.ok().body(responseService);
//        } catch (InvalidTokenException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
//        } catch (FieldsAreNullException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
//        }
//    }
//
//    @PostMapping(value = "/enable/{id}")
//    public ResponseEntity<?> enable(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
//        try {
//            TextResponse response = serviceService.enable(authorizationHeader, id);
//
//            return ResponseEntity.ok().body(response);
//        } catch (InvalidTokenException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
//        } catch (AlreadySelectedDataException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
//        }
//    }
//
//    @PostMapping(value = "/disable/{id}")
//    public ResponseEntity<?> disable(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
//        try {
//            TextResponse response = serviceService.disable(authorizationHeader, id);
//
//            return ResponseEntity.ok().body(response);
//        } catch (InvalidTokenException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
//        } catch (DisabledDataException e) {
//            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
//
//            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
//        }
//    }
//
//}
