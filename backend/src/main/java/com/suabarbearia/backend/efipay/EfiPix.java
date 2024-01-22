package com.suabarbearia.backend.efipay;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.suabarbearia.backend.config.Credentials;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EfiPix {

    public EfiPix() {
    }

    public JSONObject generatePix(Credentials credentials, String debtorName, String debtorCPF, String receiverPixKey, String chargeAmount, String description) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

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

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateCharge", params, body);
            System.out.println(response);

            return response;
        } catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    public JSONObject generateQRCode(Credentials credentials, String id) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        // Create a param and set txid
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi= new EfiPay(options);
            JSONObject response = efi.call("pixGenerateQRCode", params, new JSONObject());
            System.out.println(response);

            return response;
        } catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    public JSONObject detailPix(Credentials credentials, String txid) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        // Create a param and set txid
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("txid", txid);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixDetailCharge", params, new JSONObject());
            System.out.println(response);

            return response;
        } catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    public JSONObject refundPix(Credentials credentials, String e2eId, String id, String chargeAmount) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("e2eId", e2eId);
        params.put("id", id);

        JSONObject body = new JSONObject();
        body.put("valor", chargeAmount);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixDevolution", params, body);
            System.out.println(response);

            return response;
        } catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    // Webhook PIX
    public JSONObject configureWebhook(Credentials credentials, String pixKey, String webhookUrl) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        // Create a param and set chave (pixKey)
        HashMap<String, String> params = new HashMap<>();
        params.put("chave", pixKey);

        JSONObject body = new JSONObject();
        body.put("webhookUrl", webhookUrl);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixConfigWebhook", params, body);
            System.out.println(response);

            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    public JSONObject detailWebhook(Credentials credentials, String receiverPixKey) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        // Create a param and set chave (pixKey)
        HashMap<String, String> params = new HashMap<>();
        params.put("chave", receiverPixKey);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixDetailWebhook", params, new JSONObject());
            System.out.println(response);

            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    // Helper
    private JSONObject buildOptions(Credentials credentials) {
        JSONObject options = new JSONObject();
        options.put("client_id", credentials.getClientId());
        options.put("client_secret", credentials.getClientSecret());
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());
        options.put("x-skip-mtls-checking", "true");
        return options;
    }

}