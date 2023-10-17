package com.suabarbearia.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	// All methods have been push to our interface
}
