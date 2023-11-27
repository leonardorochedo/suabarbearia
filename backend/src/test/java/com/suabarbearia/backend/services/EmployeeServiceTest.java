package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.CreateEmployeeDto;
import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.dtos.ServiceDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.EmployeeRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;

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

    public LocalTime openTime = LocalTime.of(8, 0);
    public LocalTime closeTime = LocalTime.of(18, 0);

    @Test
    public void testFindById() {
        // Arrange
        Barbershop mockBarbershop = new Barbershop(null, "Barbearia Teste", "fulano_barber_employee@email.com", "123321", "33981111", null, "555 Main Street", "86000-000", openTime, closeTime);
        Employee mockEmployee = new Employee(null, "employee", "123321", "Funcionario Teste", null, "33983333", mockBarbershop);
        barbershopRepository.save(mockBarbershop);
        employeeRepository.save(mockEmployee);

        // Act
        Employee response = employeeService.findById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    public void testCreate() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_employee2@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
        CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee2", "123321", "123321", "33983333");

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Employee> response2 = employeeService.create(response1.getToken(), createEmployeeMock);

        assertEquals("Funcionário criado com sucesso!", response2.getMessage());
    }
}
