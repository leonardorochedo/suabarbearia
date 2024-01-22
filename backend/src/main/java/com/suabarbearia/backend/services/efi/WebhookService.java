package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import com.suabarbearia.backend.utils.EmailService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    @Autowired
    private Credentials credentials;

    @Autowired
    private EmailService emailService;

    @Value("${pix.key}")
    private String pixKey;

    public JSONObject configureWebhook() throws Exception {

        EfiPix efiPix = new EfiPix();

        JSONObject response = efiPix.configureWebhook(credentials, pixKey, "https://suabarbearia-render.onrender.com/webhook");

        System.out.println(response);

        return response;
    }

    public JSONObject webhookRecieved(String body) {

        JSONObject response = new JSONObject(body);

        System.out.println(response);

        return response;
    }

}
