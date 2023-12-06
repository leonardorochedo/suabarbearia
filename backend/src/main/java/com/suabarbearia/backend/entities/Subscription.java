package com.suabarbearia.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_subscription")
public class Subscription implements Serializable {

    // Serializable
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idSubscription; // efi sub

    @ManyToOne
    @JoinColumn(name = "monthlyplan_id")
    @JsonIgnore
    private MonthlyPlan monthlyplan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "barbershop_id")
    @JsonIgnore
    private Barbershop barbershop;

    private String title;
    private Double price;
    private String status;

    public Subscription() {}

    public Subscription(Long idSubscription, MonthlyPlan monthlyplan, User user, String title, Double price, String status) {
        this.idSubscription = idSubscription;
        this.monthlyplan = monthlyplan;
        this.user = user;
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSubscription() {
        return idSubscription;
    }

    public void setIdSubscription(Long idSubscription) {
        this.idSubscription = idSubscription;
    }

    public MonthlyPlan getMonthlyplan() {
        return monthlyplan;
    }

    public void setMonthlyplan(MonthlyPlan monthlyplan) {
        this.monthlyplan = monthlyplan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Barbershop getBarbershop() {
        return barbershop;
    }

    public void setBarbershop(Barbershop barbershop) {
        this.barbershop = barbershop;
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

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", idSubscription=" + idSubscription +
                ", monthlyplan=" + monthlyplan +
                ", user=" + user +
                ", barbershop=" + barbershop +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }

}
