package com.suabarbearia.backend.dtos;

import java.time.LocalDateTime;

public class SchedulingDto {

    private Long barbershopId;
    private Long serviceId;
    private LocalDateTime date;

    public SchedulingDto () {}

    public SchedulingDto(Long barbershopId, Long serviceId, LocalDateTime date) {
        super();
        this.barbershopId = barbershopId;
        this.serviceId = serviceId;
        this.date = date;
    }

    public Long getBarbershopId() {
        return barbershopId;
    }

    public void setBarbershopId(Long barbershopId) {
        this.barbershopId = barbershopId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
