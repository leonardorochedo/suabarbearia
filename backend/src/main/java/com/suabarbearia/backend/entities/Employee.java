package com.suabarbearia.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Arrays;
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

    @Column(unique = true)
    private String username;

    private String password;
    private String name;
    private String image;
    private String phone;

    @ManyToOne
    @JoinColumn(name="barbershop_id")
    @JsonIgnore
    private Barbershop barbershop;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Scheduling> schedulings;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<BlockSchedule> blockSchedules;

    public Employee() {

    }

    public Employee(Long id, String username, String password, String name, String image, String phone, Barbershop barbershop) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.barbershop = barbershop;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Set<BlockSchedule> getBlockSchedules() {
        return blockSchedules;
    }

    public void setBlockSchedules(Set<BlockSchedule> blockSchedules) {
        this.blockSchedules = blockSchedules;
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

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", phone='" + phone + '\'' +
                ", barbershop=" + barbershop +
                ", schedulings=" + schedulings +
                '}';
    }

}
