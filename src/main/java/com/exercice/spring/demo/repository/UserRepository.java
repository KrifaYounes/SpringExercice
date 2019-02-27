package com.exercice.spring.demo.repository;

import com.exercice.spring.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

     List<User> findByLastNameAndFirstName(String firstName, String lastName);
     // la requête généré va être du type
     // select * from user where first_name= ‘xxx’ and last_name = ‘xxxx’;
     // est-ce que c’est bon pour notre exercice ?
     // réponse non si par exemple l'utilisateur rentre first_name = jean et last_name = vide
     // alors la requête généré va
     // être select * from user where first_name= ‘jean’ and last_name = null;

}
