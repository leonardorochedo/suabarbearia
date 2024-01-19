package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import com.suabarbearia.backend.exceptions.efi.InsufficientMoneyException;
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

        // Generate PIX
        JSONObject pix = payment.generatePix(credentials, debtorName, debtorCPF, pixKey, chargeAmount, description);

        // Generate QRCode
        JSONObject getLoc = pix.getJSONObject("loc");
        JSONObject qrcode = payment.generateQRCode(credentials, getLoc.get("id").toString());

        // Juntando os dois JSON
        JSONObject response = new JSONObject();

        response.put("pix", pix);
        response.put("qrcode", qrcode);

        return response;
    }

    public JSONObject detailPix(String txid) throws Exception {
        EfiPix payment = new EfiPix();

        return payment.detailPix(credentials, txid);
    }

    public JSONObject refundPix(String e2eId, String id, String chargeAmount) throws Exception {
        EfiPix payment = new EfiPix();

        // Validation money
        if (Double.parseDouble(chargeAmount) < 5.00) {
            throw new InsufficientMoneyException("O valor do reembolso é menor do que a multa!");
        }

        Double chargeFinalValue = Double.parseDouble(chargeAmount) - 5.00;

        return payment.refundPix(credentials, e2eId, id, chargeFinalValue.toString());
    }

}
