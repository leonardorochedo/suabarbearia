package com.suabarbearia.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Barbershop;

public interface BarbershopRepository extends JpaRepository<Barbershop, Long> {
}
