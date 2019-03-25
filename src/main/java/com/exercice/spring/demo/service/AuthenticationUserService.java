package com.exercice.spring.demo.service;

import com.exercice.spring.demo.domain.Role;
import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// UserDetailsService classe qui vient de spring security
// on doit implémenté la interface UserDetailsService dans une classe avec l'annotation @Service
// cette interface contient une méthode loadUserByUsername
// on doit redefinir la méthode loadUserByUsername pour dire a spring qu'est ce qu'on doit faire
// lorsqu'on soumet le formulaire POST /login
@Service
public class AuthenticationUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Boolean.TRUE,
                true,
                true,
                true,
                authorities);
    }

}
