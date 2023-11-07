package com.suabarbearia.backend.repositories;

import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {

    Scheduling[] findAllByUser(User user);

    Scheduling[] findAllByBarbershop(Barbershop barbershop);
}
