package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.dtos.SchedulingDto;
import com.suabarbearia.backend.dtos.ServiceDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
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
    private UserService userService;

    @Test
    public void testCreate() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_scheduling@email.com", "123321", "123321", "33981111", "555 Av Brasil");
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Barba", 25.0);
        CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_scheduling@email.com", "123321", "123321", "33981111");

        ApiTokenResponse<Barbershop> response1 = barbershopService.signout(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
        ApiTokenResponse<User> response3 = userService.signout(createUserMock);

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalTime hour = LocalTime.of(8, 0, 0);

        SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), tomorrow.with(hour));

        ApiResponse<Scheduling> response4 = schedulingService.create(response3.getToken(), schedulingMock);

        assertEquals("Agendamento realizado com sucesso!", response4.getMessage());
        assertEquals(1L, response4.getData().getId());
    }

}
