package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_scheduling")
public class Scheduling implements Serializable {

	// Serializable
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@ManyToOne
	@JoinColumn(name = "barbershop_id")
	@JsonIgnore
	private Barbershop barbershop;

	@ManyToOne
	@JoinColumn(name = "service_id")
	@JsonIgnore
	private Service service;

	private LocalDateTime date;

	private boolean done;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	public Scheduling() {}

	public Scheduling(User user, Barbershop barbershop, Service service, LocalDateTime date, boolean done) {
		super();
		this.user = user;
		this.barbershop = barbershop;
		this.service = service;
		this.date = date;
		this.done = done;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return "Scheduling id=" + id + ", user=" + user + ", barbershop=" + barbershop + ", service=" + service
				+ ", date=" + date + ", done=" + done;
	}

}
