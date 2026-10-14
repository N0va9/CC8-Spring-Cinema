package com.dawan.cinema.services;

import com.dawan.cinema.dtos.UserDto;

/** Service métier des comptes utilisateurs. */
public interface UserService extends CRUDService<UserDto> {

    /**
     * Change le mot de passe d'un utilisateur après vérification de l'actuel.
     */
    void changePassword(String username, String currentPassword, String newPassword);
}