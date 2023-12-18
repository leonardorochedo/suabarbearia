package com.suabarbearia.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_monthlyplan")
public class MonthlyPlan implements Serializable {

    // Serializable
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPlan; // efiplan
    private String title;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "barbershop_id")
    @JsonIgnore
    private Barbershop barbershop;

    @OneToMany(mappedBy = "monthlyplan", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Service> servicesAllowed;

    @OneToMany(mappedBy = "monthlyplan", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Subscription> clientsSubscription;

    @OneToMany(mappedBy = "monthlyplan")
    @JsonIgnore
    private Set<Scheduling> schedulings;

    public MonthlyPlan() {}

    public MonthlyPlan(Long id, Long idPlan, String title, Double price, Barbershop barbershop, Set<Service> servicesAllowed) {
        super();
        this.id = id;
        this.idPlan = idPlan;
        this.title = title;
        this.price = price;
        this.barbershop = barbershop;
        this.servicesAllowed = servicesAllowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Barbershop getBarbershop() {
        return barbershop;
    }

    public void setBarbershop(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    public Set<Service> getServicesAllowed() {
        return servicesAllowed;
    }

    public void setServicesAllowed(Set<Service> servicesAllowed) {
        this.servicesAllowed = servicesAllowed;
    }

    public Set<Subscription> getClientsSubscription() {
        return clientsSubscription;
    }

    public void setClientsSubscription(Set<Subscription> clientsSubscription) {
        this.clientsSubscription = clientsSubscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthlyPlan that)) return false;
        return Objects.equals(idPlan, that.idPlan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlan);
    }

    @Override
    public String toString() {
        return "MonthlyPlan{" +
                "id=" + id +
                ", idPlan=" + idPlan +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", barbershop=" + barbershop +
                ", servicesAllowed=" + servicesAllowed +
                '}';
    }
}
