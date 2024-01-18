package com.suabarbearia.backend.resources.efi;

import com.suabarbearia.backend.dtos.efi.GeneratePixBody;
import com.suabarbearia.backend.services.efi.PixService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pix")
public class PixResource {

    @Autowired
    private PixService pixService;

    @PostMapping(value = "/generate-pix")
    public ResponseEntity<?> generatePix(@RequestBody GeneratePixBody body) {
        try {
            JSONObject response = pixService.generatePix(body.getDebtorName(), body.getDebtorCPF(), body.getChargeAmount(), body.getDescription());

            return ResponseEntity.ok().body(response.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
