package com.suabarbearia.backend.dtos;

public class CreateServiceDto {

    private String title;
    private Double price;

    public CreateServiceDto() {}

    public CreateServiceDto(String title, Double price) {
        super();
        this.title = title;
        this.price = price;
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

}
