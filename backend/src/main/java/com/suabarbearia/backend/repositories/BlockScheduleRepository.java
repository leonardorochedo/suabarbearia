package com.suabarbearia.backend.repositories;

import com.suabarbearia.backend.entities.BlockSchedule;
import com.suabarbearia.backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BlockScheduleRepository extends JpaRepository<BlockSchedule, Long> {
    Set<BlockSchedule> findAllByEmployee(Employee employee);
}
