package com.suabarbearia.backend.services;

import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BarbershopService barbershopService;

    @Autowired
    private BarbershopRepository barbershopRepository;

    @Test
    public void testFindById() {
        // Arrange
        Barbershop mockBarbershop = new Barbershop(null, "Barbearia Teste", "fulano_barber_employee@email.com", "123321", "33981111", null, "555 Main Street");
        Employee mockEmployee = new Employee(null, "employee", "123321", "Funcionario Teste", null, "33983333", mockBarbershop);
        barbershopRepository.save(mockBarbershop);
        employeeRepository.save(mockEmployee);

        // Act
        Employee response = employeeService.findById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
    }
}
