package com.suabarbearia.backend.services;

import java.util.Optional;

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

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Value("${fixedsalt}")
	private String fixedSalt;
	
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.get();
	}
	
	public ApiTokenResponse<User> signout(CreateUserDto user) {
		User userFinded = userRepository.findByEmail(user.getEmail());
		
		// Check data
		if (userFinded != null) {
			throw new ExistUserException("Usuário existente!");
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
}
