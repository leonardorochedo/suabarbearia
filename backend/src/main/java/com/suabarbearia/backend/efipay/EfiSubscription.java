package com.suabarbearia.backend.efipay;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.suabarbearia.backend.config.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class EfiSubscription {

    public EfiSubscription() {
    }

    public JSONObject createSubscription(Credentials credentials, String idPlan, String paymentToken, String planName, Integer planValue, String name, String cpf, String phoneNumber, String email, String birthDate, String address, Integer number, String neighborhood, String cep, String city, String state, String notificationUrl) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", idPlan);

        // items
        JSONArray items = new JSONArray();
        JSONObject item1 = new JSONObject();
        item1.put("name", planName);
        item1.put("amount", 1);
        item1.put("value", planValue);
        items.put(item1);

        //customer
        JSONObject customer = new JSONObject();
        customer.put("name", name);
        customer.put("cpf", cpf);
        customer.put("phone_number", phoneNumber);
        customer.put("email", email);
        customer.put("birth", birthDate); // 1990-05-04

        //address
        JSONObject billingAddress = new JSONObject();
        billingAddress.put("street", address);
        billingAddress.put("number", number);
        billingAddress.put("neighborhood", neighborhood);
        billingAddress.put("zipcode", cep);
        billingAddress.put("city", city);
        billingAddress.put("state", state);

        //notification URL
        JSONObject metadata = new JSONObject();
        metadata.put("notification_url", notificationUrl);
        // metadata.put("custom_id", "");

        JSONObject creditCard = new JSONObject();
        creditCard.put("installments", 1); // parcelas
        creditCard.put("billing_address", billingAddress);
        creditCard.put("payment_token", paymentToken);
        creditCard.put("customer", customer);

        JSONObject payment = new JSONObject();
        payment.put("credit_card", creditCard);

        JSONObject body = new JSONObject();
        body.put("payment", payment);
        body.put("items", items);
        body.put("metadata", metadata);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("createOneStepSubscription", params, body);
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

    public JSONObject detailSubscription(Credentials credentials, String id) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("detailSubscription", params, new JSONObject());
            System.out.println(response);

            return response;
        }catch (EfiPayException e){
            System.out.println(e.getCode());
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());

            throw e;
        }catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    public JSONObject cancelSubscription(Credentials credentials, String id) throws Exception {

        // Get credentials set in options payment
        JSONObject options = buildOptions(credentials);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("cancelSubscription", params, new JSONObject());
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
