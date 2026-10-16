package com.dawan.cinema.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawan.cinema.entities.Room;

/** Accès aux données des salles. Le cinéma est chargé en jointure pour éviter le problème N+1. */
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Override
    @EntityGraph(attributePaths = "cinema")
    Page<Room> findAll(Pageable pageable);

    /**
     * Recherche sur le nom ou la ville du cinéma.
     *
     * @param keyword  texte recherché (correspondance partielle)
     * @param pageable page, taille et tri demandés
     */
    @EntityGraph(attributePaths = "cinema")
    @Query("""
           SELECT r FROM Room r
           WHERE LOWER(r.cinema.nom)   LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(r.cinema.ville) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Room> search(@Param("keyword") String keyword, Pageable pageable);
}