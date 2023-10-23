package com.suabarbearia.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class BarbershopServiceTest {
	
	@Autowired
	private BarbershopService barbershopService;
	
	@Autowired
	private BarbershopRepository barbershopRepository;
	
	@Test
	public void testFindById() {
		// Arrange
		Barbershop mockBarbershop = new Barbershop(null, "Fulano Cortes", "fulano@email.com", "123321", "33981111", null, "555 Main Street");
		barbershopRepository.save(mockBarbershop);
		
		// Act
		Barbershop response = barbershopService.findById(1L);
		
		// Assert
		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("fulano@email.com", response.getEmail());
	}

	@Test
	public void testSignout() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Teste Barbearia", "fulano2@email.com", "123321", "123321", "33981111", "555 Av Brasil");
		
		// Act
		ApiTokenResponse<Barbershop> response = barbershopService.signout(createBarberMock);
		
		// Assert
		assertNotNull(response);
		assertEquals("Barbearia criada com sucesso!", response.getMessage());
		assertEquals("fulano2@email.com", response.getData().getEmail());
	}
	
	@Test
	public void testSignoutWithInvalidPasswords() {
        assertThrows(IllegalArgumentException.class, () -> barbershopService.signout(new CreateBarbershopDto("Teste2 Barbearia", "fulano3@email.com", "123321", "123", "33981111", "555 Av Brasil")));
	}
	
}
