package com.suabarbearia.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.responses.TextResponse;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BarbershopService barbershopService;
	
	@Test
	public void testFindById() {
		// Arrange
		User mockUser = new User(null, "Fulano Moreira", "fulano_client@email.com", "123321", "33981111", null);
		userRepository.save(mockUser);
		
		// Act
		User response = userService.findById(1L);
		
		// Assert
		assertNotNull(response);
		assertEquals(1, response.getId());
	}
	
	@Test
	public void testSignout() {
		// Arrange
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client2@email.com", "123321", "123321", "33981111");
		
		// Act
		ApiTokenResponse<User> response = userService.signout(createUserMock);
		
		// Assert
		assertNotNull(response);
		assertEquals("Usuário criado com sucesso!", response.getMessage());
		assertEquals("fulano_client2@email.com", response.getData().getEmail());
	}
	
	@Test
	public void testSignoutWithInvalidPasswords() {
        assertThrows(IllegalArgumentException.class, () -> userService.signout(new CreateUserDto("Fulano Ferreira", "fulano_client3@email.com", "123321", "123", "33981111")));
	}

	@Test
	public void testFavBarbershop() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client3@email.com", "123321", "123321", "33981111");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client@email.com", "123321", "123321", "33981111", "555 Av Brasil");

		ApiTokenResponse<User> response1 = userService.signout(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signout(createBarberMock);

		TextResponse response3 = userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		assertEquals("Barbearia Teste adicionada aos favoritos!", response3.getMessage());
	}

	@Test
	public void testUnfavBarbershop() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client4@email.com", "123321", "123321", "33981111");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client2@email.com", "123321", "123321", "33981111", "555 Av Brasil");

		ApiTokenResponse<User> response1 = userService.signout(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signout(createBarberMock);

		TextResponse response3 = userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		TextResponse response4 = userService.deleteRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		assertEquals("Barbearia Teste removida dos favoritos!", response4.getMessage());
	}
	
}