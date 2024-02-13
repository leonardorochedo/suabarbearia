package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.ChangePasswordDto;
import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.enums.Status;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.EmailService;
import com.suabarbearia.backend.utils.ImageUploader;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
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
	private EmployeeRepository employeeRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ImageUploader imageUploader;

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

		if (barbershop == null) {
			throw new InvalidTokenException("Token inválido!");
		}

		ApiResponse<Barbershop> response = new ApiResponse<Barbershop>("Perfil carregado!", barbershop);

		return response;
	}

	public ApiTokenResponse<Barbershop> signup(CreateBarbershopDto barbershop) {

		// Check data
		if (barbershop.getName() == null || barbershop.getEmail() == null || barbershop.getDocument() == null || barbershop.getBirth() == null || barbershop.getPhone() == null || barbershop.getAddress() == null || barbershop.getOpenTime() == null || barbershop.getCloseTime() == null || barbershop.getPassword() == null || barbershop.getConfirmpassword() == null) {
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
		Barbershop newBarbershop = barbershopRepository.save(new Barbershop(null, barbershop.getName(), barbershop.getEmail(), barbershop.getDocument(), barbershop.getBirth(), barbershop.getPhone(), null, barbershop.getAddress(), barbershop.getOpenTime(), barbershop.getCloseTime(), hashedPassword));

		String token = JwtUtil.generateToken(newBarbershop.getEmail());

		ApiTokenResponse<Barbershop> response = new ApiTokenResponse<Barbershop>("Barbearia criada com sucesso!", token, newBarbershop);

		return response;
	}

	public ApiTokenResponse<Barbershop> signin(SigninDto barbershop) {

        // Check data
		if (barbershop.getEmail() == null || barbershop.getPassword() == null) {
			throw new FieldsAreNullException("E-mail ou senha não exitente!");
		}

		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());

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
		Barbershop barbershopNewEmail = barbershopRepository.findByEmail(barbershop.getEmail());

		// Check barber
		if (!barbershopToken.equals(editedBarbershop)) {
			throw new InvalidTokenException("Token inválido para esta barbearia!");
		}

		// Verify new data
		if (barbershop.getName() == null || barbershop.getEmail() == null || barbershop.getDocument() == null || barbershop.getBirth() == null || barbershop.getPhone() == null || barbershop.getAddress() == null || barbershop.getOpenTime() == null || barbershop.getCloseTime() == null || barbershop.getPassword() == null || barbershop.getConfirmpassword() == null) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!barbershopToken.getEmail().equals(barbershop.getEmail()) && barbershopNewEmail != null) {
			throw new ExistDataException("E-mail em uso!");
		}

		if (!BCrypt.checkpw(barbershop.getPassword(), editedBarbershop.getPassword())) {
			throw new PasswordDontMatchException("A senha não pode ser alterada aqui!");
		}

		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		// Update barbershop
		if(image != null && !image.isEmpty()) {
			String imageUrl = imageUploader.uploadFile(image, id.toString(), "barbershops");
			editedBarbershop.setImage(imageUrl);
		}
		editedBarbershop.setName(barbershop.getName());
		editedBarbershop.setEmail(barbershop.getEmail());
        editedBarbershop.setDocument(barbershop.getDocument());
        editedBarbershop.setBirth(barbershop.getBirth());
		editedBarbershop.setPhone(barbershop.getPhone());
		editedBarbershop.setAddress(barbershop.getAddress());
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
		if (passwords.getPassword() == null || passwords.getConfirmpassword() == null) {
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
		barbershopId.setDocument("");
        barbershopId.setBirth("");
		barbershopId.setPhone("");
		barbershopId.setImage(null);
		barbershopId.setAddress(null);
		barbershopId.setOpenTime(null);
		barbershopId.setCloseTime(null);
        barbershopId.setPassword("");

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

		Set<Employee> employees = employeeRepository.findAllByBarbershop(barbershop);

		return employees;
	}

	public Set<Service> getServicesBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Set<Service> services = serviceRepository.findAllByBarbershop(barbershop);

		return services;
	}

	public Set<Scheduling> getSchedulingsBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Set<Scheduling> schedulings = schedulingRepository.findAllByBarbershop(barbershop);

		return schedulings;
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

		if (scheduling.getStatus() == Status.FINISHED || scheduling.getStatus() == Status.CANCELED || scheduling.getStatus() == Status.FOUL || scheduling.getStatus() == Status.WAITING_PAYMENT) {
			throw new SchedulingAlreadyDoneException("Não foi possível realizar esta operação!");
		}

		scheduling.setStatus(Status.FINISHED);
		barbershop.increaseChargeAmount(scheduling.getService().getPrice());

		schedulingRepository.save(scheduling);
		barbershopRepository.save(barbershop);

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

	public double getEarnings(String authorizationHeader) {
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

		return totalEarnings;
	}

	public double getEarningsWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate) {
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

		return totalEarnings;
	}

	public String clear(Long id) {

		Barbershop barbershop = barbershopRepository.findById(id).get();

		Set<Scheduling> schedulings = schedulingRepository.findAllByBarbershop(barbershop);

		LocalDateTime currentDateTime = LocalDateTime.now();

		for (Scheduling scheduling : schedulings) {
			if (scheduling.getStatus() == Status.WAITING_PAYMENT) {
				LocalDateTime creationDateTime = scheduling.getDateGeneratePayment();
				Duration duration = Duration.between(creationDateTime, currentDateTime);

				System.out.println(duration.toMinutes());

				// Verifica se passou mais de uma hora desde a criação
				if (duration.toMinutes() > 60) {
					// Altera o status para CANCELED
					scheduling.setStatus(Status.CANCELED);
					schedulingRepository.save(scheduling);
					System.out.println("Agendamento do ID: " + scheduling.getId() + " cancelado por falta de pagamento.");
				}
			}
		}

		return "Agendamentos atualizados!";
	}

}
