package com.exercice.spring.demo.service;

import com.exercice.spring.demo.domain.Role;
import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.dto.ErrorDto;
import com.exercice.spring.demo.dto.ResponseDto;
import com.exercice.spring.demo.dto.UserDto;
import com.exercice.spring.demo.enumeration.RoleEnum;
import com.exercice.spring.demo.repository.RoleRepository;
import com.exercice.spring.demo.repository.UserRepository;
import com.exercice.spring.demo.repository.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> searchUser(UserDto dto) {
        UserSpecification userSpecification = new UserSpecification(dto);
        return userRepository.findAll(userSpecification);
    }

    public ResponseDto<User> saveUser(UserDto userDto) {
        ResponseDto<User> responseDto = new ResponseDto<>();

        // verifier si l'email existe ou pas
        if (!isEmailAlreadyExistInDatabase(userDto.getEmail())) {
            User user = new User();
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            Optional<Role> adminRole = roleRepository.findByRole(RoleEnum.ADMIN.name());

            if(adminRole.isPresent()) {
                user.addRoles(adminRole.get());
                User newUser = userRepository.save(user);
                responseDto.setObject(newUser);
            } else {
                ErrorDto error = new ErrorDto();
                error.setKey("role");
                error.setMessage("Role admin is not present in database.");
                responseDto.setErrorDto(error);
            }

        } else {
            ErrorDto error = new ErrorDto();
            error.setKey("email");
            error.setMessage("Email is already used IN DATABSE");
            responseDto.setErrorDto(error);
        }

        return responseDto;
    }


    private boolean isEmailAlreadyExistInDatabase(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}