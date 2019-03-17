package com.exercice.spring.demo;

import com.exercice.spring.demo.domain.Role;
import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.enumeration.RoleEnum;
import com.exercice.spring.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	/*
		Au démarrage du serveur on insére quelque utilisateur dans la base de données
		pour pouvoir faire des recherches
	 */
	@Override
	public void run(String... args) {
		userRepository.deleteAll();

		String passwordNotEncrypted = "123456";
		Role roleAdmin = new Role();
		roleAdmin.setRole(RoleEnum.ADMIN.name());

		Role roleNoAdmin = new Role();
		roleNoAdmin.setRole(RoleEnum.NO_ADMIN.name());

		User user1 = new User();
		user1.setFirstName("Administrateur");
		user1.setLastName("Toto");
		user1.setEmail("admin@gmail.com");
		// on insére on base de données le mot de passe encrypté
		// mais lorsque l'utilisateur va saisir son mot de passe depuis l'IHM il faut
		// qu'il saissise son mot de passe non encrypté. dans notre exemple password = 123456
		user1.setPassword(bCryptPasswordEncoder.encode(passwordNotEncrypted));
		user1.addRoles(roleAdmin);
		userRepository.save(user1);

		User user2 = new User();
		user2.setFirstName("No admin");
		user2.setLastName("Tata");
		user2.setEmail("noadmin@gmail.com");
		user2.addRoles(roleNoAdmin);
		user2.setPassword(bCryptPasswordEncoder.encode(passwordNotEncrypted));
		userRepository.save(user2);

	}
}
