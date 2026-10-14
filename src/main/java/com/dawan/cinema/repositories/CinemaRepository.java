package com.dawan.cinema.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawan.cinema.entities.Cinema;

public interface CinemaRepository extends JpaRepository<Cinema, Long>{
	Page<Cinema> FindByNom(String nom, String ville, Pageable pageable);

}
