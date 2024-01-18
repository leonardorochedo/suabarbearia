package com.suabarbearia.backend.dtos.efi;

public class GeneratePixBody {

    private String debtorName;
    private String debtorCPF;
    private String chargeAmount;
    private String description;

    public GeneratePixBody() {}

    public GeneratePixBody(String debtorName, String debtorCPF, String chargeAmount, String description) {
        this.debtorName = debtorName;
        this.debtorCPF = debtorCPF;
        this.chargeAmount = chargeAmount;
        this.description = description;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorCPF() {
        return debtorCPF;
    }

    public void setDebtorCPF(String debtorCPF) {
        this.debtorCPF = debtorCPF;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
