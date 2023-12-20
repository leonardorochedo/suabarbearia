package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_user")
public class User implements Serializable {

	// Serializable
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	@Column(unique = true)
	private String email;
	private String password;
	private String cpf;
	private String birth;
	private String phone;
	@Column(name = "image", columnDefinition = "BLOB")
	private byte[] image;
	private Address address;
	
	// Relations
	@ManyToMany(fetch = FetchType.EAGER) // Load all info in relation one to one
	@JoinTable(
			name = "user_barbershop",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "barbershop_id")
	)
	@JsonIgnore
	private Set<Barbershop> barbershops;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Scheduling> schedulings;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Subscription> subscriptions;
	
	public User() {}
	
	public User(Long id, String name, String email, String cpf, String birth, String phone, byte[] image, Address address, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.cpf = cpf;
		this.birth = birth;
		this.phone = phone;
		this.image = image;
		this.address = address;
		this.password = password;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Barbershop> getBarbershops() {
		return barbershops;
	}

	public void setBarbershops(Set<Barbershop> barbershops) {
		this.barbershops = barbershops;
	}

	public Set<Scheduling> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(Set<Scheduling> schedulings) {
		this.schedulings = schedulings;
	}

	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
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
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", cpf='" + cpf + '\'' +
				", birth='" + birth + '\'' +
				", phone='" + phone + '\'' +
				", image=" + Arrays.toString(image) +
				", address=" + address +
				'}';
	}

}
