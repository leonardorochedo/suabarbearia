package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suabarbearia.backend.enums.Status;
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

	@ManyToOne
	@JoinColumn(name = "monthlyplan_id")
	@JsonIgnore
	private MonthlyPlan monthlyplan;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonIgnore
	private Employee employee;

	private LocalDateTime date;

	private Status status;

	// Efi
	private String paymentE2eId;
	private String paymentId;
	private String paymentTXID;
	private String paymentCharge;

	public Scheduling() {}

	public Scheduling(User user, Barbershop barbershop, Employee employee, Service service, LocalDateTime date, Status status) {
		super();
		this.user = user;
		this.barbershop = barbershop;
		this.employee = employee;
		this.service = service;
		this.date = date;
		this.status = status;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public MonthlyPlan getMonthlyplan() {
		return monthlyplan;
	}

	public void setMonthlyplan(MonthlyPlan monthlyplan) {
		this.monthlyplan = monthlyplan;
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

	public String getPaymentE2eId() {
		return paymentE2eId;
	}

	public void setPaymentE2eId(String paymentE2eId) {
		this.paymentE2eId = paymentE2eId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentTXID() {
		return paymentTXID;
	}

	public void setPaymentTXID(String paymentTXID) {
		this.paymentTXID = paymentTXID;
	}

	public String getPaymentCharge() {
		return paymentCharge;
	}

	public void setPaymentCharge(String paymentCharge) {
		this.paymentCharge = paymentCharge;
	}

	@Override
	public String toString() {
		return "Scheduling{" +
				"id=" + id +
				", user=" + user +
				", barbershop=" + barbershop +
				", service=" + service +
				", monthlyplan=" + monthlyplan +
				", employee=" + employee +
				", date=" + date +
				", status=" + status +
				'}';
	}
}
