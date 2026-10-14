package com.dawan.cinema.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawan.cinema.entities.Role;
import com.dawan.cinema.entities.User;

/** Accès aux données des utilisateurs. */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByRole(Role role);
}