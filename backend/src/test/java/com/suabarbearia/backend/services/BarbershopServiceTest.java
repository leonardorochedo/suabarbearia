package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.*;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.exceptions.ExistDataException;
import com.suabarbearia.backend.exceptions.InvalidDataException;
import com.suabarbearia.backend.exceptions.PasswordDontMatchException;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

	@Autowired
	private ServiceService serviceService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SchedulingService schedulingService;

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
		Barbershop mockBarbershop = new Barbershop(null, "Barbearia Teste", "fulano_barber@email.com", "17820849000104", "1998-09-12", "33981111", null, mockAddress, openTime, closeTime, "123321");
		barbershopRepository.save(mockBarbershop);
		
		// Act
		Barbershop response = barbershopService.findById(1L);
		
		// Assert
		assertNotNull(response);
		assertEquals(1, response.getId());
	}

	@Test
	public void testSignup() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber2@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		
		// Act
		ApiTokenResponse<Barbershop> response = barbershopService.signup(createBarberMock);
		
		// Assert
		assertNotNull(response);
		assertEquals("Barbearia criada com sucesso!", response.getMessage());
		assertEquals("fulano_barber2@email.com", response.getData().getEmail());
	}

	@Test
	public void testSignupWithExistsEmail() {
		barbershopService.signup(new CreateBarbershopDto("Barbearia Teste", "fulano_barber3@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321"));
		assertThrows(ExistDataException.class, () -> barbershopService.signup(new CreateBarbershopDto("Barbearia Teste", "fulano_barber3@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321")));
	}

	@Test
	public void testSignupWithInvalidPasswords() {
        assertThrows(PasswordDontMatchException.class, () -> barbershopService.signup(new CreateBarbershopDto("Barbearia Teste", "fulano_barber4@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "12321")));
	}
	
	@Test
	public void testSignin() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber4@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		SigninDto signinBarberMock = new SigninDto("fulano_barber4@email.com", "123321");

		// Act
		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signin(signinBarberMock);

		// Assert
		assertNotNull(response2);
		assertEquals("Barbearia logada com sucesso!", response2.getMessage());
		assertEquals("fulano_barber4@email.com", response2.getData().getEmail());
   	}
	
	@Test
	public void testSigninWithInvalidPassword() {
		// Arrange
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber5@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");

		// Act
		ApiTokenResponse<Barbershop> response = barbershopService.signup(createBarberMock);

		assertThrows(InvalidDataException.class, () -> barbershopService.signin(new SigninDto("fulano_barber5@email.com", "123abc")));
	}

	@Test
	public void testEditWithNoImage() throws SQLException, IOException {
		// Create
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber6@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);

		// Image
		MultipartFile image = new MockMultipartFile("arquivo", new byte[0]);

		// Edit
		EditBarbershopDto editedBarberMock = new EditBarbershopDto("Barbearia Teste", "fulano_barber6@email.com", "17820849000104", "1998-09-12", "33981111", image, mockAddress, openTime, closeTime, "123321", "123321");


		ApiResponse<Barbershop> response2 = barbershopService.edit(response1.getToken(), response1.getData().getId(), editedBarberMock, image);

		// Assert
		assertNotNull(response2);
		assertEquals("Barbearia editada com sucesso!", response2.getMessage());
		assertEquals("fulano_barber6@email.com", response2.getData().getEmail());
	}

	@Test
	public void testDelete() throws IOException {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber7@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);

		TextResponse response2 = barbershopService.delete(response1.getToken(), response1.getData().getId());

		assertEquals("Barbearia deletada com sucesso!", response2.getMessage());
	}

	@Test
	public void testGetProfile() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber8@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);

		ApiResponse<Barbershop> response2 = barbershopService.profile(response1.getToken());

		assertEquals("Perfil carregado!", response2.getMessage());
		assertEquals("fulano_barber8@email.com", response2.getData().getEmail());
	}

	@Test
	public void testGetUsersBarbershop() {
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber9@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");

		ApiTokenResponse<User> response1 = userService.signup(createUserMock);
		ApiTokenResponse<Barbershop> response2 = barbershopService.signup(createBarberMock);

		userService.createRelationWithBarbershop(response1.getToken(), response2.getData().getId());

		Set<User> response3 = barbershopService.getUsersBarbershop(response2.getToken());

		assertEquals(1, response3.size());
	}

	@Test
	public void testGetEmployeesBarbershop() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber10@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber", "123321", "123321", "33983333", 15.0);

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Employee> response2 = employeeService.create(response1.getToken(), createEmployeeMock);

		Set<Employee> response3 = barbershopService.getEmployeesBarbershop(response1.getData().getId());

		assertEquals(1, response3.size());
	}

	@Test
	public void testGetServicesBarbershop() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber11@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Sombrancelha", 30.0);

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);

		Set<Service> response3 = barbershopService.getServicesBarbershop(response1.getData().getId());

		assertEquals(1, response3.size());
	}

	@Test
	public void testGetSchedulingsBarbershop() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber12@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Degradê", 50.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber2", "123321", "123321", "33983333", 15.0);
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber2@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		Set<SchedulingReturnDto> response6 = barbershopService.getSchedulingsBarbershop(response1.getToken());

		assertEquals(1, response6.size());
	}

	@Test
	public void testGetSchedulingsBarbershopWithDate() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber13@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Cabelo e Barba", 50.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber3", "123321", "123321", "33983333", 15.0);
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber3@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

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

		Set<SchedulingReturnDto> response6 = barbershopService.getSchedulingsBarbershopWithDate(response1.getData().getId(), initialDay, endDay);

		assertEquals(1, response6.size());
	}

	@Test
	public void testConcludeScheduling() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber14@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Barba + Sombrancelha", 25.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber4", "123321", "123321", "33983333", 15.0);
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber4@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		TextResponse response6 = barbershopService.concludeScheduling(response1.getToken(), response4.getData().getId());

		assertEquals("Agendamento concluído com sucesso!", response6.getMessage());
	}

	@Test
	public void testMarkFoulScheduling() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber15@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Cabelo + Barba + Sombrancelha", 25.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber5", "123321", "123321", "33983333", 15.0);
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber5@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		TextResponse response6 = barbershopService.markFoulScheduling(response1.getToken(), response4.getData().getId());

		assertEquals("Operação realizada com sucesso!", response6.getMessage());
	}

	@Test
	public void testGetEarnings() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber16@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Degradê + Barba + Sombrancelha", 50.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber6", "123321", "123321", "33983333", 15.0);
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber6@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		TextResponse response6 = barbershopService.concludeScheduling(response1.getToken(), response5.getData().getId());
		double response7 = barbershopService.getEarnings(response1.getToken());

		assertEquals("Agendamento concluído com sucesso!", response6.getMessage());
		assertEquals(50.00, response7);
	}

	@Test
	public void testGetEarningsWithDate() {
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber17@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ServiceDto createServiceMock = new ServiceDto("Corte Degradê + Barba + Sombrancelha", 50.0);
		CreateEmployeeDto createEmployeeMock = new CreateEmployeeDto("Funcionario Teste", "employee_barber7", "123321", "123321", "33983333", 15.0);
		CreateUserDto createUserMock = new CreateUserDto("Fulano Moreira", "fulano_client_barber7@email.com", "11122233345", "2000-01-13", "33981111", mockAddress, "123321", "123321");

		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);
		ApiResponse<Service> response2 = serviceService.create(response1.getToken(), createServiceMock);
		ApiResponse<Employee> response3 = employeeService.create(response1.getToken(), createEmployeeMock);
		ApiTokenResponse<User> response4 = userService.signup(createUserMock);

		// Scheduling
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
		LocalTime hour = LocalTime.of(8, 0, 0);

		SchedulingDto schedulingMock = new SchedulingDto(response1.getData().getId(), response2.getData().getId(), response3.getData().getId(), tomorrow.with(hour));

		ApiResponse<Scheduling> response5 = schedulingService.create(response4.getToken(), schedulingMock);

		TextResponse response6 = barbershopService.concludeScheduling(response1.getToken(), response5.getData().getId());

		LocalDate initialDay = LocalDate.now().plusDays(1);
		LocalDate endDay = LocalDate.now().plusDays(5);

		double response7 = barbershopService.getEarningsWithDate(response1.getToken(), initialDay, endDay);

		assertEquals("Agendamento concluído com sucesso!", response6.getMessage());
		assertEquals(50.00, response7);
	}

	@Test
	public void testChangePassword() {
		// Create
		CreateBarbershopDto createBarberMock = new CreateBarbershopDto("Barbearia Teste", "fulano_barber18@email.com", "17820849000104", "1998-09-12", "33981111", mockAddress, openTime, closeTime, "123321", "123321");
		ApiTokenResponse<Barbershop> response1 = barbershopService.signup(createBarberMock);

		// Change
		ChangePasswordDto changePasswordMock = new ChangePasswordDto("123456", "123456");
		TextResponse response2 = barbershopService.changePassword(response1.getToken(), changePasswordMock);

		// Assert
		assertNotNull(response2);
		assertEquals("Senha alterada com sucesso!", response2.getMessage());
	}

}
