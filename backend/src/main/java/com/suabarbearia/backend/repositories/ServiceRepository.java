package com.suabarbearia.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    // All methods have been push to our interface
    Service findByTitle(String title);
}
