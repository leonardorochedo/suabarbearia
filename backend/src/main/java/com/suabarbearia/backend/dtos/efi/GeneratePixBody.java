package com.suabarbearia.backend.dtos.efi;

public class GeneratePixBody {

    private String debtorName;
    private String debtorCPF;
    private String email;
    private String chargeAmount;
    private String description;
    private Long schedulingId;

    public GeneratePixBody() {}

    public GeneratePixBody(String debtorName, String debtorCPF, String email, String chargeAmount, String description, Long schedulingId) {
        this.debtorName = debtorName;
        this.debtorCPF = debtorCPF;
        this.email = email;
        this.chargeAmount = chargeAmount;
        this.description = description;
        this.schedulingId = schedulingId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Long getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(Long schedulingId) {
        this.schedulingId = schedulingId;
    }
}
