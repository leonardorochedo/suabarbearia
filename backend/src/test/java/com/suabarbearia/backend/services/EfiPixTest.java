package com.suabarbearia.backend.services;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.utils.EfiPix;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class EfiPixTest {

    @Autowired
    private Credentials credentials;

    @Test
    public void testGeneratePix() {

        String debtorName = "Fulano de Tal";
		String debtorCPF = "11164655906";
		String receiverPixKey = "c7a80bf8-c39e-4cad-8389-96142fca65a3";
		String chargeAmount = "100.00";
		String description = "Cobrança de serviço";

		EfiPix efipix = new EfiPix();

        try {
            JSONObject response = efipix.generatePix(credentials, debtorName, debtorCPF, receiverPixKey, chargeAmount, description);

            assertEquals(receiverPixKey, response.get("chave").toString());
            assertEquals(description, response.get("solicitacaoPagador").toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGenerateQRCode() {

        String debtorName = "Fulano de Tal";
        String debtorCPF = "11164655906";
        String receiverPixKey = "c7a80bf8-c39e-4cad-8389-96142fca65a3";
        String chargeAmount = "100.00";
        String description = "Cobrança de serviço";

        EfiPix efipix = new EfiPix();

        try {
            JSONObject response1 = efipix.generatePix(credentials, debtorName, debtorCPF, receiverPixKey, chargeAmount, description);

            JSONObject getLoc = response1.getJSONObject("loc");

            JSONObject response2 = efipix.generateQRCode(credentials, getLoc.get("id").toString());

            assertEquals(response2.get("imagemQrcode"), response2.get("imagemQrcode").toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDetailPix() {

        String debtorName = "Fulano de Tal";
        String debtorCPF = "11164655906";
        String receiverPixKey = "c7a80bf8-c39e-4cad-8389-96142fca65a3";
        String chargeAmount = "100.00";
        String description = "Cobrança de serviço";

        EfiPix efipix = new EfiPix();

        try {
            JSONObject response1 = efipix.generatePix(credentials, debtorName, debtorCPF, receiverPixKey, chargeAmount, description);
            JSONObject response2 = efipix.detailPix(credentials, response1.get("txid").toString());

            assertEquals(response2.get("status"), "ATIVA");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
