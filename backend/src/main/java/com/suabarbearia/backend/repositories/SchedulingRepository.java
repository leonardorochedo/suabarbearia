package com.suabarbearia.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
}
