package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.ServiceDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class ServiceServiceTest {

    @Autowired
    private BarbershopService barbershopService;

    @Autowired
    private ServiceService serviceService;

    public LocalTime openTime = LocalTime.of(8, 0);
    public LocalTime closeTime = LocalTime.of(18, 0);

    @Test
    public void testFindById() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_service@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo", 25.0);

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);

        Service response3 = serviceService.findById(1L);

        assertEquals(1L, response3.getId());
    }

    @Test
    public void testCreate() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_service2@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo", 25.0);

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);

        assertEquals("Corte Cabelo", response2.getData().getTitle());
        assertEquals("Serviço criado com sucesso!", response2.getMessage());
    }

    @Test
    public void testEdit() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_service3@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo", 25.0);
        ServiceDto editServiceMock = new ServiceDto("Corte Cabelo e Barba", 50.0);

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
        ApiResponse<Service> response3 = serviceService.edit(response1.getToken(), response2.getData().getId(), editServiceMock);

        assertEquals("Corte Cabelo e Barba", response3.getData().getTitle());
        assertEquals("Serviço editado com sucesso!", response3.getMessage());
    }

    @Test
    public void testEnableAndDisable() {
        CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_service4@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
        ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Sobrancelha", 50.0);

        ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
        ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);

        TextResponse response3 = serviceService.disable(response1.getToken(), response2.getData().getId());
        TextResponse response4 = serviceService.enable(response1.getToken(), response2.getData().getId());

        assertEquals("Serviço Corte Cabelo + Sobrancelha desabilitado!", response3.getMessage());
        assertEquals("Serviço Corte Cabelo + Sobrancelha habilitado!", response4.getMessage());
    }

}
