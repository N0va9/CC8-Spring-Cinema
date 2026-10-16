package com.dawan.cinema.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawan.cinema.entities.Movie;

/** Accès aux données des films. */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Recherche insensible à la casse sur le titre ou le genre.
     *
     * @param keyword  texte recherché (correspondance partielle)
     * @param pageable page, taille et tri demandés
     */
    @Query("""
           SELECT m FROM Movie m
           WHERE LOWER(m.titre) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Movie> search(@Param("keyword") String keyword, Pageable pageable);
}