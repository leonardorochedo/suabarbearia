package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.dtos.BlockScheduleDto;
import com.suabarbearia.backend.dtos.SchedulingDto;
import com.suabarbearia.backend.entities.BlockSchedule;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.services.BlockScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/blockschedule")
public class BlockScheduleResource {

    @Autowired
    private BlockScheduleService blockScheduleService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<BlockSchedule> findById(@PathVariable Long id) {
        BlockSchedule blockSchedule = blockScheduleService.findById(id);

        return ResponseEntity.ok().body(blockSchedule);
    }

    @PostMapping(value = "/employee/block")
    public ResponseEntity<?> employeeBlock(@RequestHeader("Authorization") String authorizationHeader, @RequestBody BlockScheduleDto BlockSchedule) {
        try {
            ApiResponse<BlockSchedule> response = blockScheduleService.employeeBlock(authorizationHeader, BlockSchedule);

            return ResponseEntity.ok().body(response);
        } catch (NoPermissionException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
        } catch (FieldsAreNullException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
        } catch (InvalidDataException | TimeException | AlreadySelectedDataException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
        }
    }

    @PostMapping(value = "/barbershop/block")
    public ResponseEntity<?> barbershopBlock(@RequestHeader("Authorization") String authorizationHeader, @RequestBody BlockScheduleDto BlockSchedule) {
        try {
            ApiResponse<BlockSchedule> response = blockScheduleService.barbershopBlock(authorizationHeader, BlockSchedule);

            return ResponseEntity.ok().body(response);
        } catch (NoPermissionException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
        } catch (FieldsAreNullException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
        } catch (InvalidDataException | TimeException | AlreadySelectedDataException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
        }
    }

    @PatchMapping(value = "/employee/delete/{id}")
    public ResponseEntity<?> employeeBlockDelete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        try {
            TextResponse response = blockScheduleService.employeeBlockDelete(authorizationHeader, id);

            return ResponseEntity.ok().body(response);
        } catch (NoPermissionException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
        }
    }

    @PatchMapping(value = "/barbershop/delete/{id}")
    public ResponseEntity<?> barbershopBlockDelete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        try {
            TextResponse response = blockScheduleService.barbershopBlockDelete(authorizationHeader, id);

            return ResponseEntity.ok().body(response);
        } catch (NoPermissionException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(errorResponse);
        }
    }

}
