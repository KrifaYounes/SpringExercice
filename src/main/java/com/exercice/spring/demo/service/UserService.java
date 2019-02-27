package com.exercice.spring.demo.service;

import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.dto.UserDto;
import com.exercice.spring.demo.repository.UserRepository;
import com.exercice.spring.demo.repository.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> searchUser(UserDto dto) {
        UserSpecification userSpecification = new UserSpecification(dto);
        //return userRepository.findByLastNameAndFirstName(dto.getFirstName(), dto.getLastName());

        // Genération d'une requete dynamique
        return userRepository.findAll(userSpecification);

    }

}
