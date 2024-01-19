package com.suabarbearia.backend.dtos.efi;

public class RefundPixBody {

    private String e2eId;
    private String id;
    private String chargeAmount;
    private String name;
    private String email;

    public RefundPixBody() {}

    public RefundPixBody(String e2eId, String id, String chargeAmount, String name, String email) {
        this.e2eId = e2eId;
        this.id = id;
        this.chargeAmount = chargeAmount;
        this.name = name;
        this.email = email;
    }

    public String getE2eId() {
        return e2eId;
    }

    public void setE2eId(String e2eId) {
        this.e2eId = e2eId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
