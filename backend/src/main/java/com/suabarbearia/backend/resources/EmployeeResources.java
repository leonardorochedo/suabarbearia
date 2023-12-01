package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.CreateEmployeeDto;
import com.suabarbearia.backend.dtos.SigninEmployeeDto;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeResources {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);

        return ResponseEntity.ok().body(employee);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CreateEmployeeDto employee) {
        try {
            ApiResponse<Employee> response = employeeService.create(authorizationHeader, employee);

            return ResponseEntity.ok().body(response);
        }catch (NoPermissionException | InvalidTokenException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
        } catch (ExistDataException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
        } catch (FieldsAreNullException | PasswordDontMatchException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
        }
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@RequestBody SigninEmployeeDto employee) {
        try {
            ApiTokenResponse<Employee> response = employeeService.signin(employee);

            return ResponseEntity.ok().body(response);
        } catch (InvalidDataException | FieldsAreNullException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
        } catch (ResourceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorResponse);
        }
    }



}
