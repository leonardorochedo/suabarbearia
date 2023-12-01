package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.ChangePasswordDto;
import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.enums.Status;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.EmailService;
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

	@Autowired
	private EmailService emailService;

	@Value("${fixedsalt}")
	private String fixedSalt;

	public List<Barbershop> findAll() {
		List<Barbershop> barbershops = barbershopRepository.findAll();

		return barbershops;
	}

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

		// Check data
		if (barbershop.getName().isEmpty() || barbershop.getEmail().isEmpty() || barbershop.getPassword().isEmpty() || barbershop.getConfirmpassword().isEmpty() || barbershop.getPhone().isEmpty() || barbershop.getAddress().isEmpty() || barbershop.getCep().isEmpty() || barbershop.getOpenTime() == null || barbershop.getCloseTime() == null) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());
		User userFinded = userRepository.findByEmail(barbershop.getEmail());

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
		Barbershop newBarbershop = barbershopRepository.save(new Barbershop(null, barbershop.getName(), barbershop.getEmail(), barbershop.getPassword(), barbershop.getPhone(), null, barbershop.getAddress(), barbershop.getCep(), barbershop.getOpenTime(), barbershop.getCloseTime()));

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
		if (barbershop.getName().isEmpty() || barbershop.getEmail().isEmpty() || barbershop.getPassword().isEmpty() || barbershop.getConfirmpassword().isEmpty() || barbershop.getPhone().isEmpty() || barbershop.getAddress().isEmpty() || barbershop.getCep().isEmpty() || barbershop.getOpenTime() == null || barbershop.getCloseTime() == null) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if(!BCrypt.checkpw(barbershop.getPassword(), editedBarbershop.getPassword())) {
			throw new PasswordDontMatchException("A senha não pode ser alterada aqui!");
		}

		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		// Update barbershop
		editedBarbershop.setImage(image.getBytes());
		editedBarbershop.setName(barbershop.getName());
		editedBarbershop.setEmail(barbershop.getEmail());
		editedBarbershop.setPhone(barbershop.getPhone());
		editedBarbershop.setAddress(barbershop.getAddress());
		editedBarbershop.setCep(barbershop.getCep());
		editedBarbershop.setOpenTime(barbershop.getOpenTime());
		editedBarbershop.setCloseTime(barbershop.getCloseTime());

		barbershopRepository.save(editedBarbershop);

		ApiResponse<Barbershop> response = new ApiResponse<Barbershop>("Barbearia editada com sucesso!", editedBarbershop);

		return response;
	}

	public TextResponse sendEmailPassword(String email) {

		// Get user and token
		Barbershop recipient = barbershopRepository.findByEmail(email);

		if (recipient == null) {
			throw new ResourceNotFoundException("Barbearia não encontrada!");
		}

		String token = JwtUtil.generateTokenWhenForgotPassword(recipient.getEmail());

		// Send email
		String subject = "Recuperação de Senha - Sua Barbearia";
		String body = String.format(
				"Olá %s,\n\n"
						+ "Recebemos uma solicitação de recuperação de senha para a sua conta na Sua Barbearia. "
						+ "Se você não fez essa solicitação, ignore este e-mail. Caso contrário, clique no link abaixo para "
						+ "redefinir sua senha:\n\n"
						+ "Link para recuperação de senha: https://www.suabarbearia.com.br/barbershops/forgotpassword?token=%s\n\n"
						+ "Este link é válido por 1 hora.\n\n"
						+ "Atenciosamente,\n"
						+ "Equipe Sua Barbearia",
				recipient.getName(), token);

		String emailResponse = emailService.sendEmail(email, subject, body);

		TextResponse response = new TextResponse(emailResponse);

		return response;
	}

	public TextResponse changePassword(String authorizationHeader, ChangePasswordDto passwords) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		// Check data
		if (passwords.getPassword().isEmpty() || passwords.getConfirmpassword().isEmpty()) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!passwords.getPassword().equals(passwords.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		if(BCrypt.checkpw(passwords.getPassword(), barbershopToken.getPassword())) {
			throw new PasswordDontMatchException("A senha não pode ser igual a anterior!");
		}

		// Encypt and hash pass
		String hashedPassword = BCrypt.hashpw(passwords.getPassword(), fixedSalt);
		barbershopToken.setPassword(hashedPassword);

		barbershopRepository.save(barbershopToken);

		TextResponse response = new TextResponse("Senha alterada com sucesso!");

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
		barbershopId.setCep("");
		barbershopId.setOpenTime(null);
		barbershopId.setCloseTime(null);

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
