package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.CreateEmployeeDto;
import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.dtos.ServiceDto;
import com.suabarbearia.backend.entities.*;
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
    public Address mockAddress = new Address(
            "86000000",
            "Rua das Flores",
            123,
            "Centro",
            "Londrina",
            "PR",
            "Apto 456"
    );

    @Test
    public void testFindById() {
        // Arrange
        Barbershop mockBarbershop = new Barbershop(null, "Barbearia Teste", "fulano_barber_employee@email.com", "17820849000104", "1998-09-12", "33981111", null, mockAddress, openTime, closeTime, "123321");
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
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_employee2@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
        CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee2", "123321", "123321", "33983333");

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Employee> response2 = employeeService.create(response1.getToken(), createEmployeeMock);

        assertEquals("Funcionário criado com sucesso!", response2.getMessage());
    }
}
