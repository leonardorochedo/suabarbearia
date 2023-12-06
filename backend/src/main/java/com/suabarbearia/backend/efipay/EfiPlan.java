package com.suabarbearia.backend.efipay;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.suabarbearia.backend.config.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class EfiPlan {

    public EfiPlan() {
    }

    public JSONObject createPlan(Credentials credentials, String name, Integer interval, Integer repeats) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("interval", interval);
        body.put("repeats", repeats);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("createPlan", new HashMap<String,String>(), body);
            System.out.println(response);

            return response;
        } catch (EfiPayException e){
            System.out.println(e.getCode());
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    public JSONObject deletePlan(Credentials credentials, String id) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("deletePlan", params, new JSONObject());
            System.out.println(response);

            return response;
        } catch (EfiPayException e){
            System.out.println(e.getCode());
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
        options.put("sandbox", credentials.isSandbox());
        return options;
    }

}
