package com.suabarbearia.backend.resources.efi;

import br.com.efi.efisdk.exceptions.EfiPayException;
import com.suabarbearia.backend.exceptions.efi.InvalidStatusException;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.exceptions.efi.InsufficientMoneyException;
import com.suabarbearia.backend.services.efi.PixService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/pix")
public class PixResource {

    @Autowired
    private PixService pixService;

    @PostMapping(value = "/generate-pix/{id}")
    public ResponseEntity<?> generatePix(@PathVariable Long id) {
        try {
            JSONObject response = pixService.generatePix(id);

            return ResponseEntity.ok().body(response.toString());
        } catch (NoSuchElementException e) {
            ErrorResponse errorResponse = new ErrorResponse("Agendamento não localizado!");

            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
        } catch (InvalidStatusException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
        } catch (EfiPayException  e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/detail-pix/{id}")
    public ResponseEntity<?> detailPix(@PathVariable String id) {
        try {
            JSONObject response = pixService.detailPix(id);

            return ResponseEntity.ok().body(response.toString());
        } catch (EfiPayException  e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/refund-pix/{id}")
    public ResponseEntity<?> refundPix(@PathVariable Long id) {
        try {
            JSONObject response = pixService.refundPix(id);

            return ResponseEntity.ok().body(response.toString());
        } catch (InsufficientMoneyException | InvalidStatusException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorResponse);
        } catch (EfiPayException  e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
