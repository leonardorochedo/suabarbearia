package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PixService {

    @Autowired
    private Credentials credentials;

    @Value("${pix.key}")
    private String pixKey;

    public JSONObject generatePix(String debtorName, String debtorCPF, String chargeAmount, String description) throws Exception {
        EfiPix payment = new EfiPix();

        return payment.generatePix(credentials, debtorName, debtorCPF, pixKey, chargeAmount, description);
    }
}
