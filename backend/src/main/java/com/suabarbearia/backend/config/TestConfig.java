package com.suabarbearia.backend.config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.repositories.ServiceRepository;
import com.suabarbearia.backend.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BarbershopRepository barbershopRepository;
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private SchedulingRepository schedulingRepository;

	@Override
	public void run(String... args) throws Exception {
		
//		// Users
//		User u1 = new User(null, "Leonardo Rochedo", "leonardo@email.com", "123321", "33981111", null);
//		User u2 = new User(null, "Gui Molari", "gui@email.com", "123321", "33982222", null);
//
//		userRepository.saveAll(Arrays.asList(u1, u2)); // Save a list of users in testdb when initialize app
//		
//		// Barbershops
//		Barbershop b1 = new Barbershop(null, "Moreira Cortes", "moreirabarber@email.com", "123321", "33981111", null, "123 Main Street");
//		Barbershop b2 = new Barbershop(null, "Oliveira Barbearia", "oliveirabarber@email.com", "123321", "33981111", null, "500 Main Street");
//		Barbershop b3 = new Barbershop(null, "Ze Lucas", "zelucas@email.com", "123321", "33981111", null, "10 Main Street");
//		
//		barbershopRepository.saveAll(Arrays.asList(b1, b2, b3));
//
//		// Services
//		Service s1 = new Service(null, "Corte Navalhado", 29.9, b1);
//	
//		serviceRepository.save(s1);
//		
//		// Add relations
//		Set<Barbershop> barbershops = new HashSet<Barbershop>();
//		
//		barbershops.add(b1);
//		barbershops.add(b2);
//		
//		u1.setBarbershops(barbershops);
//		
//		userRepository.saveAll(Arrays.asList(u1, u2));
//		
//		// Scheduling
//		Scheduling scheduling = new Scheduling();
//		
//		scheduling.setUser(u1);
//		scheduling.setBarbershop(b1);
//		scheduling.setService(s1);
//		
//		LocalDateTime date = LocalDateTime.of(2023, 10, 23, 15, 30);
//		
//		scheduling.setDate(date);
//		
//		schedulingRepository.save(scheduling);

	}

}