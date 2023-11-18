package com.suabarbearia.backend.services;

import com.suabarbearia.backend.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
}
