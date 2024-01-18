package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.services.WebhookService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/webhook")
public class WebhookResource {

    @Autowired
    private WebhookService webhookService;

    @PostMapping
    public ResponseEntity<?> configureWebhook() {
        try {
            JSONObject response = webhookService.configureWebhook();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
