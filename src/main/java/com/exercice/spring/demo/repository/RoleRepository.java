package com.exercice.spring.demo.repository;

import com.exercice.spring.demo.domain.Role;
import com.exercice.spring.demo.enumeration.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(String role);
}
