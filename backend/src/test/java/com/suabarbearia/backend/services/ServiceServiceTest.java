package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.CreateServiceDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class ServiceServiceTest {

    @Autowired
    private BarbershopService barbershopService;

    @Autowired
    private ServiceService serviceService;

    @Test
    public void testCreate() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_service@email.com", "123321", "123321", "33981111", "555 Av Brasil");
        CreateServiceDto createServiceMock = new CreateServiceDto("Corte Cabelo", 25.0);

        ApiTokenResponse<Barbershop> response1 = barbershopService.signout(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);

        assertEquals(1L, response2.getData().getId());
        assertEquals("Corte Cabelo", response2.getData().getTitle());
        assertEquals("Serviço criado com sucesso!", response2.getMessage());
    }
}