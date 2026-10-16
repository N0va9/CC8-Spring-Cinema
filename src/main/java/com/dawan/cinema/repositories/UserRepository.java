package com.dawan.cinema.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawan.cinema.entities.Role;
import com.dawan.cinema.entities.User;

/** Accès aux données des utilisateurs. */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByRole(Role role);

	long countByRole(Role role);

	/**
	 * Recherche insensible à la casse sur l'identifiant, l'e-mail ou le nom.
	 *
	 * @param keyword  texte recherché (correspondance partielle)
	 * @param pageable page, taille et tri demandés
	 */
	@Query("""
		SELECT u FROM User u
		WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
			OR LOWER(u.email)    LIKE LOWER(CONCAT('%', :keyword, '%'))
			OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		""")
	Page<User> search(@Param("keyword") String keyword, Pageable pageable);
}