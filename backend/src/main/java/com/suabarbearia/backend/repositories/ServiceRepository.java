package com.suabarbearia.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
