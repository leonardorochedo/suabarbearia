package com.suabarbearia.backend.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
	private String email;
	private String password;
	private String phone;
	@Column(name = "image", columnDefinition = "BLOB")
	private byte[] image;
	
	// Relations
	@ManyToMany(fetch = FetchType.EAGER) // Load all info in relation one to one
	@JoinTable(
			name = "user_barbershop",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "barbershop_id")
	)
	@JsonManagedReference
	private Set<Barbershop> barbershops;
	
	@OneToMany(mappedBy = "user")
	private Set<Scheduling> schedulings;
	
	public User() {}
	
	public User(Long id, String name, String email, String password, String phone, byte[] image) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.image = image;
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
		return "User id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", phone=" + phone
				+ ", image=" + image;
	}
	
}