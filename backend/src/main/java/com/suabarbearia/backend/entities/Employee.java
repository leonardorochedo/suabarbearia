package com.suabarbearia.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_employee")
public class Employee implements Serializable {

    // Serializable
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;

    private String phone;

    @ManyToOne
    @JoinColumn(name="barbershop_id")
    @JsonIgnore
    private Barbershop barbershop;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Scheduling> schedulings;

    public Employee() {

    }

    public Employee(Long id, String username, String password, String name, byte[] image, String phone, Barbershop barbershop, Set<Scheduling> schedulings) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.barbershop = barbershop;
        this.schedulings = schedulings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Barbershop getBarbershop() {
        return barbershop;
    }

    public void setBarbershop(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    public Set<Scheduling> getSchedulings() {
        return schedulings;
    }

    public void setSchedulings(Set<Scheduling> schedulings) {
        this.schedulings = schedulings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(username, employee.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


}
