package com.dawan.cinema.controllers.web.forms;

/**
 * Contrat commun des formulaires d'administration.
 *
 * @param <D> type du DTO produit par le formulaire
 */
public interface AdminForm<D> {

    Long getId();

    void setId(Long id);

    /** Convertit le formulaire en DTO pour le service. */
    D toDto();
}