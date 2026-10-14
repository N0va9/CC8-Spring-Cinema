package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Opérations CRUD génériques, exposées uniquement sous forme de DTO 
 * @param <D> type du DTO
 */
public interface CRUDService<D> {

    Page<D> getAll(Pageable pageable);

    D getById(Long id);

    D create(D dto);

    D update(Long id, D dto);

    void delete(Long id);

}
