package com.dawan.cinema.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dawan.cinema.entities.User;


public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(String username);

}
