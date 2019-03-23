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

// UserDetailsService classe qui vient de spring security
// on doit implémenté la interface UserDetailsService dans une classe avec l'annotation @Service
// cette interface contient une méthode loadUserByUsername
// on doit redefinir la méthode loadUserByUsername pour dire a spring qu'est ce qu'on doit faire
// lorsqu'on soumet le formulaire POST /login
@Service
public class UserService implements UserDetailsService {

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

    @Override
    @Transactional // l'objetif de cette méthode est de retourner l'objet UserDETAILS qui est un objet
    // de spring de spring security.
    // cette objet UserDetails doit contenir l'email le password crypté et la liste des roles de l'utilisateur
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // on recupere le user de la base de doonées à partir de son mail
        Optional<User> user = userRepository.findByEmail(userName);
        if(user.isPresent()) {
            // on recupere la liste des role de l'utilisateur
            Set<Role> userRoles = user.get().getRoles();

            // on convert la list des roles en list de SimpleGrantedAuthority ( qui est la
            // classe de spring security qui contient les roles
            List<SimpleGrantedAuthority> authorities = getUserAuthority(userRoles);

            UserDetails userDetails = getUserDetails(user.get(), authorities);

            return userDetails;
        } else {
            throw new UsernameNotFoundException("message user n'existe pas");
        }
    }

    private List<SimpleGrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        // interface GrantedAuthority vient de spring security
        // qui va contenir les roles de l'utilisateur qui sont définis dans la base de doonées

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Role role : userRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return grantedAuthorities;
    }

    private UserDetails getUserDetails(User user, List<SimpleGrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Boolean.TRUE,
                true,
                true,
                true,
                authorities);
    }

    private boolean isEmailAlreadyExistInDatabase(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}