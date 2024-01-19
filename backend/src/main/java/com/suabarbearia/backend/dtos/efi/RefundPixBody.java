package com.suabarbearia.backend.dtos.efi;

public class RefundPixBody {

    private String e2eId;
    private String id;
    private String chargeAmount;

    public RefundPixBody() {}

    public RefundPixBody(String e2eId, String id, String chargeAmount) {
        this.e2eId = e2eId;
        this.id = id;
        this.chargeAmount = chargeAmount;
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

}
