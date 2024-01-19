package com.suabarbearia.backend.resources.efi;

import com.suabarbearia.backend.services.efi.WebhookService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/webhook")
public class WebhookResource {

    @Autowired
    private WebhookService webhookService;

    @PutMapping
    public ResponseEntity<?> configureWebhook() {
        try {
            JSONObject response = webhookService.configureWebhook();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> recieveConfigWebhook(@RequestBody String body) {
        System.out.println(body);

        return ResponseEntity.ok().body(body);
    }

    @PostMapping(value = "/pix")
    public ResponseEntity<?> recieveConfirmationWebhook(@RequestBody String body) {
        System.out.println(body);

        return ResponseEntity.ok().body(body);
    }

}
