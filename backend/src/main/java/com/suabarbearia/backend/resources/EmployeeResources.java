package com.suabarbearia.backend.resources;

import com.suabarbearia.backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeResources {

    @Autowired
    private EmployeeService employeeService;
}
