package com.exercice.spring.demo.service;

import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.dto.UserDto;
import com.exercice.spring.demo.repository.UserRepository;
import com.exercice.spring.demo.repository.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> searchUser(UserDto dto) {
        UserSpecification userSpecification = new UserSpecification(dto);

        // Genération d'une requete dynamique
        return userRepository.findAll(userSpecification);

    }

    public User saveOrUpdate(UserDto dto) {
        UserSpecification userSpecification = new UserSpecification(dto);
        List<User> users = searchUser(dto);
        if(!CollectionUtils.isEmpty(users)) {
            User user = users.get(0);
            user.setFirstName("tototototo");
            user.setLastName("tototototo");
            return userRepository.save(user);
        }

        User user = new User();
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        return userRepository.save(user);

    }
}
