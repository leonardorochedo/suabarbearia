package com.suabarbearia.backend.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String cep;
    private String street;
    private Integer number;
    private String neighborhood;
    private String city;
    private String state;
    private String complement;

    public Address() {}

    public Address(String cep, String street, Integer number, String neighborhood, String city, String state, String complement) {
        this.cep = cep;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.complement = complement;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    @Override
    public String toString() {
        return "Address{" +
                "cep='" + cep + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", neighborhood='" + neighborhood + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", complement='" + complement + '\'' +
                '}';
    }

}
