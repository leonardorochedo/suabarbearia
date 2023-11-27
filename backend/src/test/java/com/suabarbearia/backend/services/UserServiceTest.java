package com.suabarbearia.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.suabarbearia.backend.dtos.*;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.exceptions.PasswordDontMatchException;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test") // Run in test file
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BarbershopService barbershopService;

	@Autowired
	private ServiceService serviceService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SchedulingService schedulingService;

	public LocalTime openTime = LocalTime.of(8, 0);
	public LocalTime closeTime = LocalTime.of(18, 0);
	
	@Test
	public void testFindById() {
		// Arrange
		User mockUser = new User(null, "Fulano Moreira", "fulano_client@email.com", "123321", "33981111", null, "Av. Brasil 111", "86000-000");
		userRepository.save(mockUser);
		
		// Act
		User response = userService.findById(1L);
		
		// Assert
		assertNotNull(response);
		assertEquals(1, response.getId());
	}
	
	@Test
	public void testSignup() {
		// Arrange
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client2@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		
		// Act
		ApiTokenResponse<User> response = userService.signup(createUserMock);
		
		// Assert
		assertNotNull(response);
		assertEquals("Usuário criado com sucesso!", response.getMessage());
		assertEquals("fulano_client2@email.com", response.getData().getEmail());
	}
	
	@Test
	public void testSignupWithInvalidPasswords() {
        assertThrows(PasswordDontMatchException.class, () -> userService.signup(new CreateUserDto("Fulano Ferreira", "fulano_client3@email.com", "123321", "123", "33981111", "Av. Brasil 111", "86000-000")));
	}

	@Test
	public void testSignin() {
		// Arrange
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client3@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		SigninDto signinUserMock = new SigninDto("fulano_client3@email.com", "123321");

		// Act
		ApiTokenResponse<User> response1 = userService.signup(createUserMock);
		ApiTokenResponse<User> response2 = userService.signin(signinUserMock);

		// Assert
		assertNotNull(response2);
		assertEquals("Usuário logado com sucesso!", response2.getMessage());
		assertEquals("fulano_client3@email.com", response2.getData().getEmail());
	}

	@Test
	public void testGetProfile() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client4@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");

		ApiTokenResponse<User> response1 = userService.signup(createUserMock);
		ApiResponse<User> response2 = userService.profile(response1.getToken());

		assertEquals("Perfil carregado!", response2.getMessage());
		assertEquals("fulano_client4@email.com", response2.getData().getEmail());
	}

	@Test
	public void testEditWithNoImage() throws SQLException, IOException {
		// Create
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client5@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		ApiTokenResponse<User> response1 = userService.signup(createUserMock);

		// Edit
		EditUserDto editedUserMock = new EditUserDto("Moreira Fulano", "fulano_client5@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000", null);

		// Image
		MultipartFile image = Mockito.mock(MultipartFile.class);

		ApiResponse<User> response2 = userService.edit(response1.getToken(), response1.getData().getId(), editedUserMock, image);

		// Assert
		assertNotNull(response2);
		assertEquals("Usuário editado com sucesso!", response2.getMessage());
		assertEquals("fulano_client5@email.com", response2.getData().getEmail());
	}

	@Test
	public void testDelete() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client6@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		ApiTokenResponse<User> response1 = userService.signup(createUserMock);

		TextResponse response2 = userService.delete(response1.getToken(), response1.getData().getId());

		assertEquals("Usuário deletado com sucesso!", response2.getMessage());
	}

	@Test
	public void testFavBarbershop() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client7@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);

		ApiTokenResponse<User> response1 = userService.signup(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signup(createBarberMock);

		TextResponse response3 = userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		assertEquals("Barbearia Teste adicionada aos favoritos!", response3.getMessage());
	}

	@Test
	public void testUnfavBarbershop() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client8@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client2@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);

		ApiTokenResponse<User> response1 = userService.signup(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signup(createBarberMock);

		TextResponse response3 = userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		TextResponse response4 = userService.deleteRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		assertEquals("Barbearia Teste removida dos favoritos!", response4.getMessage());
	}

	@Test
	public void testGetSchedulingsUser() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client3@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
		ServiceDto createServiceMock = new ServiceDto("Corte Degradê", 50.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_client", "123321", "123321", "33983333");
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client9@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		Set<Scheduling> response6 = userService.getSchedulingsUser(response4.getToken());

		assertEquals(1, response6.size());
	}

	@Test
	public void testGetSchedulingsUserWithDate() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client4@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);
		ServiceDto createServiceMock = new ServiceDto("Corte Degradê + Barba", 50.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_client2", "123321", "123321", "33983333");
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client10@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		LocalDate initialDay = LocalDate.now().plusDays(1);
		LocalDate endDay = LocalDate.now().plusDays(5);

		Set<Scheduling> response6 = userService.getSchedulingsUserWithDate(response4.getToken(), initialDay, endDay);

		assertEquals(1, response6.size());
	}

	@Test
	public void testGetBarbershops() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client11@email.com", "123321", "123321", "33981111", "Av. Brasil 111", "86000-000");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber_client5@email.com", "123321", "123321", "33981111", "555 Av Brasil", "86000-000", openTime, closeTime);

		ApiTokenResponse<User> response1 = userService.signup(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signup(createBarberMock);

		TextResponse response3 = userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		Set<Barbershop> response4 = userService.getBarbershopsUser(response1.getToken());

		assertEquals("Barbearia Teste adicionada aos favoritos!", response3.getMessage());
		assertEquals(1, response4.size());
	}

}
