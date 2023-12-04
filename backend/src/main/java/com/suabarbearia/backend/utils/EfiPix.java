package com.suabarbearia.backend.utils;

import br.com.efi.efisdk.EfiPay;
import com.suabarbearia.backend.config.Credentials;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class EfiPix {

    public EfiPix() {
    }

    public JSONObject generatePix(Credentials credentials, String debtorName, String debtorCPF, String receiverPixKey, String chargeAmount, String description) throws Exception {

        // Get credentials set in options payment
        JSONObject options = new JSONObject();
        options.put("client_id", credentials.getClientId());
        options.put("client_secret", credentials.getClientSecret());
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        // Generate transaction id
        UUID uuid = UUID.randomUUID();
        String txId = uuid.toString().replace("-", "");

        // Transaction parameters
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", txId.substring(0, 32).toString());

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600)); // 1 hour to expire
        body.put("devedor", new JSONObject()
                .put("cpf", debtorCPF)
                .put("nome", debtorName));
        body.put("valor", new JSONObject().put("original", chargeAmount));
        body.put("chave", receiverPixKey); // Changed to receiverPixKey
        body.put("solicitacaoPagador", description);

        // Generate pixCharge
        EfiPay efi = new EfiPay(options);

        return efi.call("pixCreateCharge", params, body);
    }

}
