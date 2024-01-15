package com.suabarbearia.backend.dtos;

import java.time.LocalDateTime;

public class BlockScheduleDto {

    private Long idBarbershop;
    private Long idEmployee;
    private LocalDateTime date;

    public BlockScheduleDto() {}

    public BlockScheduleDto(Long idBarbershop, Long idEmployee, LocalDateTime date) {
        this.idBarbershop = idBarbershop;
        this.idEmployee = idEmployee;
        this.date = date;
    }

    public Long getIdBarbershop() {
        return idBarbershop;
    }

    public void setIdBarbershop(Long idBarbershop) {
        this.idBarbershop = idBarbershop;
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
