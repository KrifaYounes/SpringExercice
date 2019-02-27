package com.exercice.spring.demo;

import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		// une map permet de stocker des objets de la maniére key, value
		// une map fonction fonctionne avec une key et une value
		// Map<key,value>
		// une map ne peut contenir qu'une seule clé dans sa liste
		// impossible d'avoir deux clé identiques avec deux value
		Map<User, String> map = new HashMap<>();
		User user1 = new User();
		user1.setLastName("firstName");
		User user2 = new User();
		user2.setLastName("firstName");
		map.put(user1, "Bonjour");
		map.put(user2, "Bonjour 2");
		System.out.println(map.size()); //la taille de la map est de un
	}

	@Autowired
	private UserRepository userRepository;


	/*
		Au démarrage du serveur on insére quelque utilisateur dans la base de données
		pour pouvoir faire des recherches
	 */
	@Override
	public void run(String... args) {
		User user1 = new User();
		user1.setFirstName("toto");
		user1.setLastName("tata");
		userRepository.save(user1);

		User user2 = new User();
		user2.setFirstName("BONJOUR");
		user2.setLastName("allo");
		userRepository.save(user2);

		User user3 = new User();
		user3.setFirstName("des");
		user3.setLastName("pacito");
		userRepository.save(user3);

		User user4 = new User();
		user4.setFirstName("world");
		user4.setLastName("company");
		userRepository.save(user4);
	}
}
