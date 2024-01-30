package com.suabarbearia.backend.repositories;

import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);

    Set<Employee> findAllByBarbershop(Barbershop barbershop);
}
