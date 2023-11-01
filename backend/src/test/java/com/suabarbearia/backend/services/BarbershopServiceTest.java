package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class BarbershopServiceTest {
	
	@Autowired
	private BarbershopService barbershopService;
	
	@Autowired
	private BarbershopRepository barbershopRepository;

	@Autowired
	private UserService userService;
	
	@Test
	public void testFindById() {
		// Arrange
		Barbershop mockBarbershop = new Barbershop(null, "Barbearia Teste", "fulano_barber@email.com", "123321", "33981111", null, "555 Main Street");
		barbershopRepository.save(mockBarbershop);
		
		// Act
		Barbershop response = barbershopService.findById(1L);
		
		// Assert
		assertNotNull(response);
		assertEquals(1, response.getId());
	}

	@Test
	public void testSignout() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber2@email.com", "123321", "123321", "33981111", "555 Av Brasil");
		
		// Act
		ApiTokenResponse<Barbershop> response = barbershopService.signout(createBarberMock);
		
		// Assert
		assertNotNull(response);
		assertEquals("Barbearia criada com sucesso!", response.getMessage());
		assertEquals("fulano_barber2@email.com", response.getData().getEmail());
	}

	@Test
	public void testSignoutWithInvalidEmail() {
		assertThrows(IllegalArgumentException.class, () -> barbershopService.signout(new CreateBarbershopDto("Barbearia Teste", "fulano_barber3@email.com", "123321", "123", "33981111", "555 Av Brasil")));
	}

	@Test
	public void testSignoutWithInvalidPasswords() {
        assertThrows(IllegalArgumentException.class, () -> barbershopService.signout(new CreateBarbershopDto("Barbearia Teste", "fulano_barber3@email.com", "123321", "123", "33981111", "555 Av Brasil")));
	}
	
	@Test
	public void testSignin() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber3@email.com", "123321", "123321", "33981111", "555 Av Brasil");
		SigninDto signinBarberMock = new SigninDto("fulano_barber3@email.com", "123321");

		// Act
		ApiTokenResponse<Barbershop> response1 = barbershopService.signout(createBarberMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signin(signinBarberMock);

		// Assert
		assertNotNull(response2);
		assertEquals("Barbearia logada com sucesso!", response2.getMessage());
		assertEquals("fulano_barber3@email.com", response2.getData().getEmail());
   	}
	
	@Test
	public void testSigninWithInvalidPassword() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber4@email.com", "123321", "123321", "33981111", "555 Av Brasil");

		// Act
		ApiTokenResponse<Barbershop> response = barbershopService.signout(createBarberMock);

		assertThrows(IllegalArgumentException.class, () -> barbershopService.signin(new SigninDto("fulano_barber4@email.com", "123abc")));
	}

	@Test
	public void testEditWithNoImage() throws SQLException, IOException {
		// Create
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber5@email.com", "123321", "123321", "33981111", "555 Av Brasil");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signout(createBarberMock);

		// Edit
		EditBarbershopDto editedBarberMock = new EditBarbershopDto(null, "Barbearia Teste", "fulano_barber6@email.com", "123321", "123321", "33981111", "101 Av Brasil");

		// Image
		MultipartFile image = Mockito.mock(MultipartFile.class);

		ApiResponse<Barbershop> response2 = barbershopService.edit(response1.getToken(), 1L, editedBarberMock, image);

		// Assert
		assertNotNull(response2);
		assertEquals("Barbearia editada com sucesso!", response2.getMessage());
		assertEquals("fulano_barber6@email.com", response2.getData().getEmail());
		assertEquals("101 Av Brasil", response2.getData().getAddress());
	}

	@Test
	public void testDelete() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber7@email.com", "123321", "123321", "33981111", "555 Av Brasil");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signout(createBarberMock);

		TextResponse response2 = barbershopService.delete(response1.getToken(), response1.getData().getId());

		assertEquals("Barbearia deletada com sucesso!", response2.getMessage());
	}

	@Test
	public void testGetProfile() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber8@email.com", "123321", "123321", "33981111", "555 Av Brasil");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signout(createBarberMock);

		ApiResponse<Barbershop> response2 = barbershopService.profile(response1.getToken());

		assertEquals("Perfil carregado!", response2.getMessage());
		assertEquals("fulano_barber8@email.com", response2.getData().getEmail());
	}

	@Test
	public void testGetUsersBarbershop() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber@email.com", "123321", "123321", "33981111");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber9@email.com", "123321", "123321", "33981111", "555 Av Brasil");

		ApiTokenResponse<User> response1 = userService.signout(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signout(createBarberMock);

		userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		Set<User> response3 = barbershopService.getUsersBarbershop(response2.getToken());

		assertEquals(1, response3.size());
	}

}
