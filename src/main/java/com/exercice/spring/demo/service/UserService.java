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
import com.exercice.spring.demo.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> searchUser(UserDto dto) {
        UserSpecification userSpecification = new UserSpecification(dto);
        return userRepository.findAll(userSpecification);
    }

    public ResponseDto<User> saveUser(UserDto userDto) {
        ResponseDto<User> responseDto = new ResponseDto<>();

        /*if(UserUtils.isPasswordNotValid(userDto)) {
            ErrorDto error = new ErrorDto();
            error.setKey("password");
            error.setMessage("Passwords must match.");
            responseDto.setErrorDto(error);

        } else */if (!isEmailAlreadyExistInDatabase(userDto.getEmail())) {
            User user = new User();
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            Optional<Role> adminRole = roleRepository.findByRole(RoleEnum.ADMIN.name());

            if(adminRole.isPresent()) {
                user.addRoles(adminRole.get());
            } else {
                ErrorDto error = new ErrorDto();
                error.setKey("role");
                error.setMessage("Role admin is not present in database.");
                responseDto.setErrorDto(error);
            }

            User newUser = userRepository.save(user);
            responseDto.setObject(newUser);

        } else {
            ErrorDto error = new ErrorDto();
            error.setKey("email");
            error.setMessage("Email is already used");
            responseDto.setErrorDto(error);
        }

        return responseDto;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userName).get();
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Boolean.TRUE, true, true, true, authorities);
    }

    private boolean isEmailAlreadyExistInDatabase(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}