package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    @Autowired
    private Credentials credentials;

    @Value("${pix.key}")
    private String pixKey;

    public JSONObject configureWebhook(String webhookUrl) throws Exception {

        EfiPix efiPix = new EfiPix();

        JSONObject response = efiPix.configureWebhook(credentials, pixKey, webhookUrl);

        System.out.println(response);

        return response;
    }

}
