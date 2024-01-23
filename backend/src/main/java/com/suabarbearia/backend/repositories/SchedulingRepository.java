package com.suabarbearia.backend.repositories;

import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.suabarbearia.backend.entities.Scheduling;

import java.util.List;
import java.util.Set;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {

    Set<Scheduling> findAllByUser(User user);

    Set<Scheduling> findAllByBarbershop(Barbershop barbershop);

    Set<Scheduling> findAllByEmployee(Employee employee);

    List<Scheduling> findTopByUserAndBarbershopOrderByDateDesc(User user, Barbershop barbershop);

    Scheduling findByPaymentTXID(String paymentTXID);

}
