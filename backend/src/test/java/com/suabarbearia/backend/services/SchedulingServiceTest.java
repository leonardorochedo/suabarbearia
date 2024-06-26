package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.*;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class SchedulingServiceTest {

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private BarbershopService barbershopService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

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
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_scheduling@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Barba", 25.0);
        CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_scheduling", "123321", "123321", "33983333");
        CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_scheduling@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
        ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
        ApiTokenResponse<User> response4 = userService.signup(createUserMock);

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalTime hour = LocalTime.of(8, 0, 0);

        SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

        ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

        Scheduling response6 = schedulingService.findById(response5.getData().getId());

        assertEquals("Corte Cabelo + Barba", response6.getService().getTitle());
    }

    @Test
    public void testCreate() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_scheduling2@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Barba", 25.0);
        CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_scheduling2", "123321", "123321", "33983333");
        CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_scheduling2@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
        ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
        ApiTokenResponse<User> response4 = userService.signup(createUserMock);

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalTime hour = LocalTime.of(8, 0, 0);

        SchedulingDto schedulingMock = new SchedulingDto(response4.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

        ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

        assertEquals("Agendamento solicitado com sucesso!", response5.getMessage());
    }

    @Test
    public void testEdit() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_scheduling3@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Barba + Sombracelha", 25.0);
        CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_scheduling3", "123321", "123321", "33983333");

        CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_scheduling3@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
        ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
        ApiTokenResponse<User> response4 = userService.signup(createUserMock);

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(3);
        LocalTime hour = LocalTime.of(8, 0, 0);

        SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

        ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

        LocalDateTime tomorrowEdit = LocalDateTime.now().plusDays(2);

        SchedulingDto schedulingMockEdit = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrowEdit.with(hour));

        ApiResponse<Scheduling> response6 = schedulingService.edit(response4.getToken(), response5.getData().getId(), schedulingMockEdit);

        assertEquals("Agendamento alterado com sucesso!", response6.getMessage());
    }

}
