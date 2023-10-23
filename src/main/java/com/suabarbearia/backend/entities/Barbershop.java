package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_barbershop")
public class Barbershop implements Serializable {

	// Serializable
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String email;
	private String password;
	private String image;
	private String address;
	
	@OneToMany(mappedBy = "barbershop", cascade = CascadeType.ALL)
	private Set<Service> services;
	
	// Relations
	@ManyToMany
	@JoinTable(
			name = "user_barbershop",
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Set<User> clients;
	
	@OneToMany(mappedBy = "barbershop")
	private Set<Scheduling> schedulings;
	
	public Barbershop() {}

	public Barbershop(Long id, String name, String email, String password, String image, String address) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.image = image;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	public Set<User> getClients() {
		return clients;
	}

	public void setClients(Set<User> clients) {
		this.clients = clients;
	}

	public Set<Scheduling> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(Set<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Barbershop other = (Barbershop) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "Barbershop id=" + id + ", name=" + name + ", email=" + email + ", image=" + image + ", address="
				+ address + ", services=" + services + ", clients=" + clients + ", schedulings=" + schedulings;
	}

}
