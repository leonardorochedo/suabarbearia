package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
	private String document;
	private String birth;
	private String phone;
	@Column(name = "image", columnDefinition = "BLOB")
	private byte[] image;
	private Address address;
	private LocalTime openTime;
	private LocalTime closeTime;
	private Double chargeAmount;
	
	@OneToMany(mappedBy = "barbershop", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Service> services;

	@OneToMany(mappedBy = "barbershop", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<MonthlyPlan> monthlyplans;

	@OneToMany(mappedBy = "barbershop", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Subscription> subscriptions;
	
	// Relations
	@ManyToMany(fetch = FetchType.EAGER) // Load all info in relation one to one
	@JoinTable(
			name = "user_barbershop",
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	@JsonIgnore
	private Set<User> clients;
	
	@OneToMany(mappedBy = "barbershop", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Scheduling> schedulings;

	@OneToMany(mappedBy = "barbershop", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Employee> employees;
	
	public Barbershop() {}

	public Barbershop(Long id, String name, String email, String document, String birth, String phone, byte[] image, Address address, LocalTime openTime, LocalTime closeTime, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.document = document;
		this.birth = birth;
		this.phone = phone;
		this.image = image;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.password = password;
		this.chargeAmount= 0.0;
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

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}

	public Double getChargeAmount() {
		return chargeAmount;
	}

	public void increaseChargeAmount(Double chargeAmount) {
		this.chargeAmount += chargeAmount;
	}

	public void removeChargeAmount(Double chargeAmount) {
		this.chargeAmount -= chargeAmount;
	}

	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	public Set<MonthlyPlan> getMonthlyplans() {
		return monthlyplans;
	}

	public void setMonthlyplans(Set<MonthlyPlan> monthlyplans) {
		this.monthlyplans = monthlyplans;
	}

	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
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

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
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
		return "Barbershop{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", document='" + document + '\'' +
				", birth='" + birth + '\'' +
				", phone='" + phone + '\'' +
				", image=" + Arrays.toString(image) +
				", address=" + address +
				", openTime=" + openTime +
				", closeTime=" + closeTime +
				", chargeAmount=" + chargeAmount +
				'}';
	}
}
