package com.suabarbearia.backend.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suabarbearia.backend.dtos.CreateBarbershopDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.utils.JwtUtil;

@Service
public class BarbershopService {
	
	@Autowired
	private BarbershopRepository barbershopRepository;
	
	public Barbershop findById(Long id) {
		Optional<Barbershop> barbershop = barbershopRepository.findById(id);
		
		return barbershop.get();
	}
	
	public ApiTokenResponse<Barbershop> signout(CreateBarbershopDto barbershop) {
		Barbershop barberFinded = barbershopRepository.findByEmail(barbershop.getEmail());
		
		// Check data
		if (barberFinded != null) {
			throw new ExistUserException("Barbearia existente!");
		}
		
		if (barbershop.getName() == null || barbershop.getEmail() == null || barbershop.getPassword() == null || barbershop.getConfirmpassword() == null || barbershop.getPhone() == null || barbershop.getAddress() == null) {
		    throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
		}
		
		if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
			throw new IllegalArgumentException("As senhas não batem!");
		}
		
		// Encypt and hash pass
		String fixedSalt = "$2a$12$BQfBVhn6AyUbA1QljSUnU.";
	    String hashedPassword = BCrypt.hashpw(barbershop.getPassword(), fixedSalt);
	    barbershop.setPassword(hashedPassword);
	    
	    // Storage
	    Barbershop newBarbershop = barbershopRepository.save(new Barbershop(null, barbershop.getName(), barbershop.getEmail(), barbershop.getPassword(), barbershop.getPhone(), null, barbershop.getAddress()));
		
		String token = JwtUtil.generateToken(newBarbershop.getEmail());
		
		ApiTokenResponse<Barbershop> response = new ApiTokenResponse<Barbershop>("Barbearia criada com sucesso!", token, newBarbershop);
		
		return response;
	}
}
