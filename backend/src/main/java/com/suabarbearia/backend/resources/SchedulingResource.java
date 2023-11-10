package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.SchedulingDto;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.services.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/schedulings")
public class SchedulingResource {

    @Autowired
    private SchedulingService schedulingService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Scheduling> findById(@PathVariable Long id) {
        Scheduling scheduling = schedulingService.findById(id);

        return ResponseEntity.ok().body(scheduling);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SchedulingDto scheduling) {
        try {
            ApiResponse<Scheduling> response = schedulingService.create(authorizationHeader, scheduling);

            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
        }
    }

    @PatchMapping(value = "/edit/{id}")
    public ResponseEntity<?> edit(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody SchedulingDto scheduling) {
        try {
            ApiResponse<Scheduling> response = schedulingService.edit(authorizationHeader, id, scheduling);

            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
        }
    }

}
