package com.suabarbearia.backend.dtos;

import com.suabarbearia.backend.entities.Service;

import java.util.Set;

public class MonthlyPlanDto {

    private Long idPlan; // efi
    private String title;
    private Double price;
    private Set<Service> servicesAllowed;

    public MonthlyPlanDto() {}

    public MonthlyPlanDto(Long idPlan, String title, Double price, Set<Service> servicesAllowed) {
        this.idPlan = idPlan;
        this.title = title;
        this.price = price;
        this.servicesAllowed = servicesAllowed;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
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

    public Set<Service> getServicesAllowed() {
        return servicesAllowed;
    }

    public void setServicesAllowed(Set<Service> servicesAllowed) {
        this.servicesAllowed = servicesAllowed;
    }

}
