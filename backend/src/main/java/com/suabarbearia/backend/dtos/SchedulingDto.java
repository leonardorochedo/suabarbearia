package com.suabarbearia.backend.dtos;

import java.time.LocalDateTime;

public class SchedulingDto {

    private Long idBarbershop;
    private Long idService;
    private Long idMonthlyPlan;
    private Long idEmployee;
    private LocalDateTime date;

    public SchedulingDto () {}

    public SchedulingDto(Long idBarbershop, Long idService, Long idEmployee, LocalDateTime date) {
        this.idBarbershop = idBarbershop;
        this.idService = idService;
        this.idEmployee = idEmployee;
        this.date = date;
    }

    public Long getIdBarbershop() {
        return idBarbershop;
    }

    public void setIdBarbershop(Long idBarbershop) {
        this.idBarbershop = idBarbershop;
    }

    public Long getIdService() {
        return idService;
    }

    public void setIdService(Long idService) {
        this.idService = idService;
    }

    public Long getIdMonthlyPlan() {
        return idMonthlyPlan;
    }

    public void setIdMonthlyPlan(Long idMonthlyPlan) {
        this.idMonthlyPlan = idMonthlyPlan;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
