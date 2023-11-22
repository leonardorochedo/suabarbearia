package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
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
		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());
		User userFinded = userRepository.findByEmail(barbershop.getEmail());

		// Check data
		if (barberFinded != null || userFinded != null) {
			throw new ExistUserException("Conta existente!");
		}

		if (barbershop.getName() == null || barbershop.getEmail() == null || barbershop.getPassword() == null || barbershop.getConfirmpassword() == null || barbershop.getPhone() == null || barbershop.getAddress() == null) {
			throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new IllegalArgumentException("As senhas não batem!");
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
		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());

		// Check data
		if (barberFinded == null) {
			throw new ResourceNotFoundException("Barbearia não existente!");
		}

		if (!BCrypt.checkpw(barbershop.getPassword(), barberFinded.getPassword())) {
			throw new IllegalArgumentException("E-mail ou senha inválidos!");
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
			throw new RuntimeException("Token inválido para esta barbearia!");
		}

		// Verify new data
		if (barbershop.getName() == null || barbershop.getEmail() == null || barbershop.getPassword() == null || barbershop.getConfirmpassword() == null || barbershop.getPhone() == null || barbershop.getAddress() == null) {
			throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
		}

		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new IllegalArgumentException("As senhas não batem!");
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
			throw new RuntimeException("Token inválido para esta barbearia!");
		}

		barbershopRepository.deleteById(id);

		TextResponse response = new TextResponse("Barbearia deletada com sucesso!");

		return response;
	}

	public Set<User> getUsersBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getClients();
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
			throw new IllegalArgumentException("Data inválida!");
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
			throw new IllegalArgumentException("Token inválido!");
		}

		if (scheduling.isDone()) {
			throw new RuntimeException("Agendamento já concluído!");
		}

		scheduling.setDone(true);

		schedulingRepository.save(scheduling);

		TextResponse response = new TextResponse("Agendamento concluído com sucesso!");

		return response;
	}
	
}
