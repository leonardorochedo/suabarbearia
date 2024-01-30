package com.suabarbearia.backend.repositories;

import com.suabarbearia.backend.entities.Barbershop;
import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Service;

import java.util.Set;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    // All methods have been push to our interface
    Service findByTitle(String title);

    Set<Service> findAllByBarbershop(Barbershop barbershop);
}
