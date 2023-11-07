package com.suabarbearia.backend.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.dtos.SigninDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.exceptions.ResourceNotFoundException;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BarbershopService {
	
	@Autowired
	private BarbershopRepository barbershopRepository;

	@Autowired
	private UserRepository userRepository;

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

	public ApiTokenResponse<Barbershop> signout(CreateBarbershopDto barbershop) {
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
		JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop editedBarbershop = barbershopRepository.findById(id).get();

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
		JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findById(id).get();

		barbershopRepository.deleteById(id);

		TextResponse response = new TextResponse("Barbearia deletada com sucesso!");

		return response;
	}

	public Set<User> getUsersBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getClients();
	}

	public Set<com.suabarbearia.backend.entities.Service> getServicesBarbershop(String authorizationHeader) {
		String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

		Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

		return barbershop.getServices();
	}
	
}
