package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.ChangePasswordDto;
import com.suabarbearia.backend.dtos.EditUserDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.EmailService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BarbershopRepository barbershopRepository;

	@Autowired
	private SchedulingRepository schedulingRepository;

	@Autowired
	private EmailService emailService;

	@Value("${fixedsalt}")
	private String fixedSalt;
	
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.get();
	}

	public ApiResponse<User> profile(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User user = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		ApiResponse<User> response = new ApiResponse<User>("Perfil carregado!", user);

		return response;
	}

	public ApiTokenResponse<User> signup(CreateUserDto user) {

		// Check data
		if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getConfirmpassword().isEmpty() || user.getPhone().isEmpty() || user.getAddress().isEmpty() || user.getCep().isEmpty()) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		User userFinded = userRepository.findByEmail(user.getEmail());
		Barbershop barberFinded = barbershopRepository.findByEmail(user.getEmail());

		if (userFinded != null || barberFinded != null) {
			throw new ExistDataException("Conta existente!");
		}
		
		if (!user.getPassword().equals(user.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}
		
		// Encypt and hash pass
	    String hashedPassword = BCrypt.hashpw(user.getPassword(), fixedSalt);
	    user.setPassword(hashedPassword);
	    
	    // Storage
	    User newUser = userRepository.save(new User(null, user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), null, user.getAddress(), user.getCep()));
		
		String token = JwtUtil.generateToken(newUser.getEmail());
		
		ApiTokenResponse<User> response = new ApiTokenResponse<User>("Usuário criado com sucesso!", token, newUser);
		
		return response;
	}

	public ApiTokenResponse<User> signin(SigninDto user) {

		// Check data
		if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
			throw new FieldsAreNullException("E-mail ou senha não existente!");
		}

		User userFinded = userRepository.findByEmail(user.getEmail());

		if (userFinded == null) {
			throw new ResourceNotFoundException("Usuário não existente!");
		}

		if (!BCrypt.checkpw(user.getPassword(), userFinded.getPassword())) {
			throw new InvalidDataException("E-mail ou senha inválidos!");
		}

		String token = JwtUtil.generateToken(userFinded.getEmail());

		// Create a response
		ApiTokenResponse<User> response = new ApiTokenResponse<User>("Usuário logado com sucesso!", token, userFinded);

		return response;
	}

	public ApiResponse<User> edit(String authorizationHeader, Long id, EditUserDto user, MultipartFile image) throws SQLException, IOException {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User userToken = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));
		User editedUser = userRepository.findById(id).get();

		// Check user
		if (!userToken.equals(editedUser)) {
			throw new InvalidTokenException("Token inválido para este usuário!");
		}

		// Verify new data
		if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getConfirmpassword().isEmpty() || user.getPhone().isEmpty() || user.getAddress().isEmpty() || user.getCep().isEmpty()) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if(!BCrypt.checkpw(user.getPassword(), editedUser.getPassword())) {
			throw new PasswordDontMatchException("A senha não pode ser alterada aqui!");
		}

		if (!user.getPassword().equals(user.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		// Update user
		editedUser.setImage(image.getBytes());
		editedUser.setName(user.getName());
		editedUser.setEmail(user.getEmail());
		editedUser.setPhone(user.getPhone());
		editedUser.setAddress(user.getAddress());
		editedUser.setCep(user.getCep());

		userRepository.save(editedUser);

		ApiResponse<User> response = new ApiResponse<User>("Usuário editado com sucesso!", editedUser);

		return response;
	}

	public TextResponse sendEmailPassword(String email) {

		// Get user and token
		User recipient = userRepository.findByEmail(email);

		if (recipient == null) {
			throw new ResourceNotFoundException("Usuário não encontrado!");
		}

		String token = JwtUtil.generateTokenWhenForgotPassword(recipient.getEmail());

		// Send email
		String subject = "Recuperação de Senha - Sua Barbearia";
		String body = String.format(
				"Olá %s,\n\n"
				+ "Recebemos uma solicitação de recuperação de senha para a sua conta na Sua Barbearia. "
				+ "Se você não fez essa solicitação, ignore este e-mail. Caso contrário, clique no link abaixo para "
				+ "redefinir sua senha:\n\n"
				+ "Link para recuperação de senha: https://www.suabarbearia.com.br/users/forgotpassword?token=%s\n\n"
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

		User userToken = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		// Check data
		if (passwords.getPassword().isEmpty() || passwords.getConfirmpassword().isEmpty()) {
			throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!passwords.getPassword().equals(passwords.getConfirmpassword())) {
			throw new PasswordDontMatchException("As senhas não batem!");
		}

		if(BCrypt.checkpw(passwords.getPassword(), userToken.getPassword())) {
			throw new PasswordDontMatchException("A senha não pode ser igual a anterior!");
		}

		// Encypt and hash pass
		String hashedPassword = BCrypt.hashpw(passwords.getPassword(), fixedSalt);
		userToken.setPassword(hashedPassword);

		userRepository.save(userToken);

		TextResponse response = new TextResponse("Senha alterada com sucesso!");

		return response;
	}

	public TextResponse delete(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User userToken = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));
		User userId = userRepository.findById(id).get();

		// Check barber
		if (!userToken.equals(userId)) {
			throw new InvalidTokenException("Token inválido para este usuário!");
		}

		// Att data
		userId.setName("Usuário excluído!");
		userId.setEmail("");
		userId.setPassword("");
		userId.setPhone("");
		userId.setAddress("");
		userId.setCep("");
		userId.setImage(null);

		userRepository.save(userId);

		TextResponse response = new TextResponse("Usuário deletado com sucesso!");

		return response;
	}

	public TextResponse createRelationWithBarbershop(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User user = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Barbershop barbershop = barbershopRepository.findById(id).get();

		// Check barbershop
		Set<Barbershop> userBarbershops = user.getBarbershops();

		for (Barbershop userBarbershop : userBarbershops) {
			if (userBarbershop.equals(barbershop)) {
				throw new AlreadySelectedDataException(barbershop.getName() + " já está favoritada!");
			}
		}

		user.getBarbershops().add(barbershop);

		userRepository.save(user);

		TextResponse response = new TextResponse(barbershop.getName() + " adicionada aos favoritos!");

		return response;
	}

	public TextResponse deleteRelationWithBarbershop(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User user = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		Barbershop barbershop = barbershopRepository.findById(id).get();

		// Check barbershop
		Set<Barbershop> userBarbershops = user.getBarbershops();

		if (!userBarbershops.contains(barbershop)) {
			throw new DisabledDataException(barbershop.getName() + " não está favoritada!");
		}

		user.getBarbershops().remove(barbershop);

		userRepository.save(user);

		TextResponse response = new TextResponse(barbershop.getName() + " removida dos favoritos!");

		return response;
	}

	public Set<Scheduling> getSchedulingsUser(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User user = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return user.getSchedulings();
	}

	public Set<Scheduling> getSchedulingsUserWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User user = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		// Check date
		if (initialDate.isAfter(endDate) || endDate.isBefore(initialDate)) {
			throw new InvalidDataException("Data inválida!");
		}

		Set<Scheduling> schedulings = schedulingRepository.findAllByUser(user);

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

	public Set<Barbershop> getBarbershopsUser(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User user = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return user.getBarbershops();
	}

}
