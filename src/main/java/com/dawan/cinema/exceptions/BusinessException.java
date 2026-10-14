package com.dawan.cinema.exceptions;

/**
 * Levée lorsqu'une règle métier n'est pas respectée (ex. salle déjà occupée sur un créneau).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}