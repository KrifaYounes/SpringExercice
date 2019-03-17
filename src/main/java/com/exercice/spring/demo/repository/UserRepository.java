package com.exercice.spring.demo.repository;

import com.exercice.spring.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

     List<User> findByLastNameAndFirstName(String firstName, String lastName);

     Optional<User> findByEmail(String email);
}
