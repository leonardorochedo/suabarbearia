package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.enums.Status;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Service
public class BarbershopService {
	
	@Autowired
	private BarbershopRepository barbershopRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SchedulingRepository schedulingRepository;

	@Value("${fixedsalt}")
	private String fixedSalt;
	
	public Barbershop findById(Long id) {
		Optional<Barbershop> barbershop = barbershopRepository.findById(id);
		
		return barbershop.get();
	}

	public ApiResponse<Barbershop> profile(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		ApiResponse<Barbershop> response = new ApiResponse<Barbershop>("Perfil carregado!", barbershop);

		return response;
	}

	public ApiTokenResponse<Barbershop> signup(CreateBarbershopDto barbershop) {
		if (barbershop.getName().isEmpty() || barbershop.getEmail().isEmpty() || barbershop.getPassword().isEmpty() || barbershop.getConfirmpassword().isEmpty() || barbershop.getPhone().isEmpty() || barbershop.getAddress().isEmpty()) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());
		User userFinded = userRepository.findByEmail(barbershop.getEmail());

		// Check data
		if (barberFinded != null || userFinded != null) {
			throw new ExistDataException("Conta existente!");
		}

		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		// Encypt and hash pass
		String hashedPassword = BCrypt.hashpw(barbershop.getPassword(), fixedSalt);
		barbershop.setPassword(hashedPassword);

		// Storage
		Barbershop newBarbershop = barbershopRepository.save(new Barbershop(null, barbershop.getName(), barbershop.getEmail(), barbershop.getPassword(), barbershop.getPhone(), null, barbershop.getAddress()));

		String token = JwtUtil.generateToken(newBarbershop.getEmail());

		ApiTokenResponse<Barbershop> response = new ApiTokenResponse<Barbershop>("Barbearia criada com sucesso!", token, newBarbershop);

		return response;
	}

	public ApiTokenResponse<Barbershop> signin(SigninDto barbershop) {
		if (barbershop.getEmail().isEmpty() || barbershop.getPassword().isEmpty()) {
			throw new FieldsAreNullException("E-mail ou senha não exitente!");
		}

		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());

		// Check data
		if (barberFinded == null) {
			throw new ResourceNotFoundException("Barbearia não existente!");
		}

		if (!BCrypt.checkpw(barbershop.getPassword(), barberFinded.getPassword())) {
			throw new InvalidDataException("E-mail ou senha inválidos!");
		}

		String token = JwtUtil.generateToken(barbershop.getEmail());

		// Create a response
		ApiTokenResponse<Barbershop> response = new ApiTokenResponse<Barbershop>("Barbearia logada com sucesso!", token, barberFinded);

		return response;
	}

	public ApiResponse<Barbershop> edit(String authorizationHeader, Long id, EditBarbershopDto barbershop, MultipartFile image) throws SQLException, IOException {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
		Barbershop editedBarbershop = barbershopRepository.findById(id).get();

		// Check barber
		if (!barbershopToken.equals(editedBarbershop)) {
			throw new InvalidTokenException("Token inválido para esta barbearia!");
		}

		// Verify new data
		if (barbershop.getName() == null || barbershop.getEmail() == null || barbershop.getPassword() == null || barbershop.getConfirmpassword() == null || barbershop.getPhone() == null || barbershop.getAddress() == null) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		String hashedPassword = BCrypt.hashpw(barbershop.getPassword(), fixedSalt);

		// Update barbershop
		editedBarbershop.setImage(image.getBytes());
		editedBarbershop.setName(barbershop.getName());
		editedBarbershop.setEmail(barbershop.getEmail());
		editedBarbershop.setPassword(hashedPassword);
		editedBarbershop.setPhone(barbershop.getPhone());
		editedBarbershop.setAddress(barbershop.getAddress());

		barbershopRepository.save(editedBarbershop);

		ApiResponse<Barbershop> response = new ApiResponse<Barbershop>("Barbearia editada com sucesso!", editedBarbershop);

		return response;
	}

	public TextResponse delete(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
		Barbershop barbershopId = barbershopRepository.findById(id).get();

		// Check barber
		if (!barbershopToken.equals(barbershopId)) {
			throw new InvalidTokenException("Token inválido para esta barbearia!");
		}

		barbershopId.setName("Barbearia excluída!");
		barbershopId.setEmail("");
		barbershopId.setPassword("");
		barbershopId.setPhone("");
		barbershopId.setImage(null);
		barbershopId.setAddress("");

		barbershopRepository.save(barbershopId);

		TextResponse response = new TextResponse("Barbearia deletada com sucesso!");

		return response;
	}

	public Set<User> getUsersBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getClients();
	}

	public Set<Employee> getEmployeesBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getEmployees();
	}

	public Set<Service> getServicesBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getServices();
	}

	public Set<Scheduling> getSchedulingsBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getSchedulings();
	}

	public Set<Scheduling> getSchedulingsBarbershopWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		// Check date
		if (initialDate.isAfter(endDate) || endDate.isBefore(initialDate)) {
			throw new InvalidDataException("Data inválida!");
		}

		Set<Scheduling> schedulings = schedulingRepository.findAllByBarbershop(barbershop);

		Set<Scheduling> schedulingsInRange = new HashSet<>();

		for (Scheduling scheduling : schedulings) {
			LocalDate schedulingDate = scheduling.getDate().toLocalDate();

			if ((schedulingDate.isEqual(initialDate) || schedulingDate.isAfter(initialDate)) &&
					(schedulingDate.isEqual(endDate) || schedulingDate.isBefore(endDate.plusDays(1)))) {
				schedulingsInRange.add(scheduling);
			}
		}

		return schedulingsInRange;
	}

	public TextResponse concludeScheduling(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Scheduling scheduling = schedulingRepository.findById(id).get();

		if (!scheduling.getBarbershop().equals(barbershop)) {
			throw new InvalidTokenException("Token inválido!");
		}

		if (scheduling.getStatus() == Status.FINISHED || scheduling.getStatus() == Status.CANCELED || scheduling.getStatus() == Status.FOUL) {
			throw new SchedulingAlreadyDoneException("Não foi possível realizar esta operação!");
		}

		scheduling.setStatus(Status.FINISHED);

		schedulingRepository.save(scheduling);

		TextResponse response = new TextResponse("Agendamento concluído com sucesso!");

		return response;
	}

	public TextResponse markFoulScheduling(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Scheduling scheduling = schedulingRepository.findById(id).get();

		if (!scheduling.getBarbershop().equals(barbershop)) {
			throw new InvalidTokenException("Token inválido!");
		}

		if (scheduling.getStatus() == Status.FINISHED || scheduling.getStatus() == Status.CANCELED || scheduling.getStatus() == Status.FOUL) {
			throw new SchedulingAlreadyDoneException("Não foi possível realizar esta operação!");
		}

		scheduling.setStatus(Status.FOUL);

		schedulingRepository.save(scheduling);

		TextResponse response = new TextResponse("Operação realizada com sucesso!");

		return response;
	}

	public TextResponse getEarnings(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Set<Scheduling> schedulings = schedulingRepository.findAllByBarbershop(barbershop);

		double totalEarnings = 0.0;

		for (Scheduling scheduling : schedulings) {
			if (scheduling.getStatus() == Status.FINISHED) { // only done schedulings
				double servicePrice = scheduling.getService().getPrice();
				totalEarnings += servicePrice;
			}
		}

		TextResponse response = new TextResponse(String.format("Total de faturamento: R$%.2f", totalEarnings));

		return response;
	}

	public TextResponse getEarningsWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Set<Scheduling> schedulings = schedulingRepository.findAllByBarbershop(barbershop);

		double totalEarnings = 0.0;

		for (Scheduling scheduling : schedulings) {
			LocalDate schedulingDate = scheduling.getDate().toLocalDate();

			if (scheduling.getStatus() == Status.FINISHED && (schedulingDate.isEqual(initialDate) || schedulingDate.isAfter(initialDate)) &&
					(schedulingDate.isEqual(endDate) || schedulingDate.isBefore(endDate.plusDays(1)))) { // only done schedulings and is btwn dates
				double servicePrice = scheduling.getService().getPrice();
				totalEarnings += servicePrice;
			}
		}

		TextResponse response = new TextResponse(String.format("Total de faturamento deste intervalo de dias: R$%.2f", totalEarnings));

		return response;
	}
	
}
