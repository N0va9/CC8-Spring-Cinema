package com.dawan.cinema.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawan.cinema.entities.Cinema;

/* Accès aux données des cinémas */
public interface CinemaRepository extends JpaRepository<Cinema, Long>{
	@Query("""
			SELECT c FROM Cinema c
			WHERE LOWER(c.nom)	 LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(c.ville) LIKE LOWER(CONCAT('%', :keyword, '%'))
			""")
	Page<Cinema> search(@Param("keyword") String keyword, Pageable pageable);

}
