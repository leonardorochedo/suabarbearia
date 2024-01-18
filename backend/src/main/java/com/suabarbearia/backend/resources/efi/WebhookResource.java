package com.suabarbearia.backend.resources.efi;

import com.suabarbearia.backend.services.efi.WebhookService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/webhook")
public class WebhookResource {

    @Autowired
    private WebhookService webhookService;

    @PostMapping
    public ResponseEntity<?> configureWebhook(@RequestBody String webhookUrl) {
        try {
            JSONObject response = webhookService.configureWebhook(webhookUrl);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
