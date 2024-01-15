package com.suabarbearia.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_block_schedule")
public class BlockSchedule implements Serializable {

	// Serializable
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "barbershop_id")
	@JsonIgnore
	private Barbershop barbershop;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonIgnore
	private Employee employee;

	private LocalDateTime date;

	public BlockSchedule() {}

	public BlockSchedule(Barbershop barbershop, Employee employee, LocalDateTime date) {
		super();
		this.barbershop = barbershop;
		this.employee = employee;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Barbershop getBarbershop() {
		return barbershop;
	}

	public void setBarbershop(Barbershop barbershop) {
		this.barbershop = barbershop;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Scheduling{" +
				"id=" + id +
				", barbershop=" + barbershop +
				", employee=" + employee +
				", date=" + date +
				'}';
	}
}
