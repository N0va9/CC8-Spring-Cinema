package com.dawan.cinema.mappers;

import org.springframework.stereotype.Component;

import com.dawan.cinema.dtos.UserDto;
import com.dawan.cinema.entities.User;

/**
 * Conversion User <-> UserDto.
 * Le mot de passe n'est jamais recopié : ni vers le DTO (sécurité), ni vers l'entité (il doit être haché par le service).
 */
@Component
public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getRole(), null);
    }

    @Override
    public User toEntity(UserDto dto) {
        User user = new User();
        updateEntityFromDto(user, dto);
        return user;
    }

    @Override
    public void updateEntityFromDto(User user, UserDto dto) {
        user.setUsername(dto.username());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
    }
}