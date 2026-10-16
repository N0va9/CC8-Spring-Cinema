package com.dawan.cinema.exceptions;

/**
 * Levée lorsqu'une ressource demandée n'existe pas en base.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " introuvable avec l'id " + id);
    }
}