package com.suabarbearia.backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		// User u1 = new User(null, "Leonardo Rochedo", "leonardo@email.com", "123321", "33981111", null);
		// User u2 = new User(null, "Gui Molari", "gui@email.com", "123321", "33982222", null);

		// userRepository.saveAll(Arrays.asList(u1, u2)); // Save a list of users in testdb when initialize app
	}

}
