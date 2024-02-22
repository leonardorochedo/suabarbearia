package com.suabarbearia.backend.dtos;

import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.enums.Status;

import java.time.LocalDateTime;

public class SchedulingReturnDto {

    private Long id;
    private Service service;
    private Employee employee;
    private Barbershop barbershop;
    private LocalDateTime date;
    private Status status;

    public SchedulingReturnDto(Long id, Service service, Employee employee, Barbershop barbershop, LocalDateTime date, Status status) {
        this.id = id;
        this.service = service;
        this.employee = employee;
        this.barbershop = barbershop;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Barbershop getBarbershop() {
        return barbershop;
    }

    public void setBarbershop(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
