package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawan.cinema.dtos.UserDto;
import com.dawan.cinema.entities.Role;
import com.dawan.cinema.entities.User;
import com.dawan.cinema.exceptions.BusinessException;
import com.dawan.cinema.mappers.UserMapper;
import com.dawan.cinema.repositories.UserRepository;

/**
 * Implémentation du service des utilisateurs : hachage des mots de passe
 * et garde-fous sur la suppression (pas soi-même, pas le dernier administrateur).
 */
@Service
public class UserServiceImpl extends CRUDServiceImpl<User, UserDto, UserRepository> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper, User.class);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Page<User> searchByKeyword(String keyword, Pageable pageable) {
        return repository.search(keyword, pageable);
    }

    /**
     * Préparation avant enregistrement : rôle par défaut, et hachage du mot de passe s'il est fourni.
     * Un mot de passe vide en modification laisse l'ancien inchangé.
     */
    @Override
    protected void resolveRelations(User user, UserDto dto) {
        if (user.getRole() == null) {
            user.setRole(Role.ADMIN);
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        } else if (user.getPassword() == null) {
            throw new BusinessException("Le mot de passe est obligatoire.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findEntityById(id);
        if (user.getUsername().equals(currentUsername())) {
            throw new BusinessException("Vous ne pouvez pas supprimer votre propre compte.");
        }
        if (user.getRole() == Role.ADMIN && repository.countByRole(Role.ADMIN) <= 1) {
            throw new BusinessException("Impossible de supprimer le dernier administrateur.");
        }
        repository.delete(user);
    }

    @Override
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable."));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException("Le mot de passe actuel est incorrect.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    /** Identifiant de l'utilisateur connecté, ou null. */
    private static String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }
}