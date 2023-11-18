package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_service")
public class Service implements Serializable {

	// Serializable
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private Double price;
	private boolean enabled;
	
	// Relations
	@ManyToOne
    @JoinColumn(name = "barbershop_id")
	@JsonIgnore
    private Barbershop barbershop;
	
	@OneToMany(mappedBy = "service")
	@JsonIgnore
	private Set<Scheduling> schedulings;

	public Service() {}
	
	public Service(Long id, String title, Double price, Boolean enabled, Barbershop barbershop) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
		this.enabled = enabled;
		this.barbershop = barbershop;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Barbershop getBarbershop() {
		return barbershop;
	}

	public void setBarbershop(Barbershop barbershop) {
		this.barbershop = barbershop;
	}

	@Override
	public String toString() {
		return "Service{" +
				"id=" + id +
				", title='" + title + '\'' +
				", price=" + price +
				", enabled=" + enabled +
				", barbershop=" + barbershop +
				'}';
	}
}
