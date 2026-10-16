package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Opérations CRUD génériques, exposées uniquement sous forme de DTO 
 * @param <D> type du DTO
 */
public interface CRUDService<D> {

    /**
     * Liste paginée, filtrée par mot-clé si fourni
     * 
     * @param keyword mot-clé de recherche, ignoré s'il est null ou vide
     * @param pageable page, taille et tri demandés
     */
    Page<D> getAll(String keyword, Pageable pageable);

    D getById(Long id);

    D create(D dto);

    D update(Long id, D dto);

    void delete(Long id);

}
