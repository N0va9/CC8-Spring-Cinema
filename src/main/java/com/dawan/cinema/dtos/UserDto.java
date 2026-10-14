package com.dawan.cinema.dtos;

import com.dawan.cinema.entities.Role;

/**
 * Données d'un utilisateur.
 * password : en entrée uniquement (création, changement), toujours null en sortie.
 */
public record UserDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        Role role,
        String password) {

    /** Masque le mot de passe : un record affiche sinon tous ses champs (risque de fuite dans les logs). */
    @Override
    public String toString() {
        return "UserDto[id=" + id + ", username=" + username + ", email=" + email + ", role=" + role + "]";
    }
}