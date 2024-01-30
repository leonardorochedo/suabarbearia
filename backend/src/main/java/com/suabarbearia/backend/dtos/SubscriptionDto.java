package com.suabarbearia.backend.dtos;

public class SubscriptionDto {

    private Long idSubscription; // efi
    private Long idMonthlyPlan;
    private String title;
    private Double price;
    private String status;

    public SubscriptionDto() {}

    public SubscriptionDto(Long idSubscription, Long idMonthlyPlan, String title, Double price, String status) {
        this.idSubscription = idSubscription;
        this.idMonthlyPlan = idMonthlyPlan;
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public Long getIdSubscription() {
        return idSubscription;
    }

    public void setIdSubscription(Long idSubscription) {
        this.idSubscription = idSubscription;
    }

    public Long getIdMonthlyPlan() {
        return idMonthlyPlan;
    }

    public void setIdMonthlyPlan(Long idMonthlyPlan) {
        this.idMonthlyPlan = idMonthlyPlan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
