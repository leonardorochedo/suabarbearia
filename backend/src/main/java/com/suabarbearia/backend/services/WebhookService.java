package com.suabarbearia.backend.services;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    @Autowired
    private Credentials credentials;

    public JSONObject configureWebhook() throws Exception {

        EfiPix efiPix = new EfiPix();

        JSONObject response = efiPix.configureWebhook(credentials);

        System.out.println(response);

        return response;
    }

}
