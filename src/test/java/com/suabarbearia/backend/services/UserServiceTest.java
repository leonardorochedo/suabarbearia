package com.suabarbearia.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	
	@Test
	public void testFindById() {
		// Arrange
		User mockUser = new User(null, "Fulano da Silva", "fulano@email.com", "123321", "33981111", null);
		userRepository.save(mockUser);
		
		// Act
		User response = userService.findById(1L);
		
		// Assert
		assertNotNull(response);
		assertEquals(1, response.getId());
		assertEquals("fulano@email.com", response.getEmail());
	}
	
	@Test
	public void testSignout() {
		// Arrange
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano2@email.com", "123321", "123321", "33981111");
		
		// Act
		ApiTokenResponse<User> response = userService.signout(createUserMock);
		
		// Assert
		assertNotNull(response);
		assertEquals("Usuário criado com sucesso!", response.getMessage());
		assertEquals("fulano2@email.com", response.getData().getEmail());
	}
	
	@Test
	public void testSignoutWithInvalidPasswords() {
        assertThrows(IllegalArgumentException.class, () -> userService.signout(new CreateUserDto("Fulano Ferreira", "fulano3@email.com", "123321", "123", "33981111")));
	}
	
}
