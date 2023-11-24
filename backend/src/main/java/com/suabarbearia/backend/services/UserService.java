package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.EditUserDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suabarbearia.backend.dtos.CreateUserDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.exceptions.ExistUserException;
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
		User userFinded = userRepository.findByEmail(user.getEmail());
		Barbershop barberFinded = barbershopRepository.findByEmail(user.getEmail());
		
		// Check data
		if (userFinded != null || barberFinded != null) {
			throw new ExistUserException("Conta existente!");
		}
		
		if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getConfirmpassword() == null || user.getPhone() == null) {
		    throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
		}
		
		if (!user.getPassword().equals(user.getConfirmpassword())) {
			throw new IllegalArgumentException("As senhas não batem!");
		}
		
		// Encypt and hash pass
	    String hashedPassword = BCrypt.hashpw(user.getPassword(), fixedSalt);
	    user.setPassword(hashedPassword);
	    
	    // Storage
	    User newUser = userRepository.save(new User(null, user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), null));
		
		String token = JwtUtil.generateToken(newUser.getEmail());
		
		ApiTokenResponse<User> response = new ApiTokenResponse<User>("Usuário criado com sucesso!", token, newUser);
		
		return response;
	}

	public ApiTokenResponse<User> signin(SigninDto user) {
		User userFinded = userRepository.findByEmail(user.getEmail());

		// Check data
		if (userFinded == null) {
			throw new ResourceNotFoundException("Usuário não existente!");
		}

		if (!BCrypt.checkpw(user.getPassword(), userFinded.getPassword())) {
			throw new IllegalArgumentException("E-mail ou senha inválidos!");
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
			throw new RuntimeException("Token inválido para este usuário!");
		}

		// Verify new data
		if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getConfirmpassword() == null || user.getPhone() == null) {
			throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!user.getPassword().equals(user.getConfirmpassword())) {
			throw new IllegalArgumentException("As senhas não batem!");
		}

		String hashedPassword = BCrypt.hashpw(user.getPassword(), fixedSalt);

		// Update user
		editedUser.setImage(image.getBytes());
		editedUser.setName(user.getName());
		editedUser.setEmail(user.getEmail());
		editedUser.setPassword(hashedPassword);
		editedUser.setPhone(user.getPhone());

		userRepository.save(editedUser);

		ApiResponse<User> response = new ApiResponse<User>("Usuário editado com sucesso!", editedUser);

		return response;
	}

	public TextResponse delete(String authorizationHeader, Long id) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		User userToken = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));
		User userId = userRepository.findById(id).get();

		// Check barber
		if (!userToken.equals(userId)) {
			throw new RuntimeException("Token inválido para este usuário!");
		}

		// Att data
		userId.setName("Usuário excluído!");
		userId.setEmail("");
		userId.setPassword("");
		userId.setPhone("");
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
				throw new IllegalArgumentException(barbershop.getName() + " já está favoritada!");
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
			throw new IllegalArgumentException(barbershop.getName() + " não está favoritada!");
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
			throw new IllegalArgumentException("Data inválida!");
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
