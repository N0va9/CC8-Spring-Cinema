package com.dawan.cinema.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dawan.cinema.dtos.UserDto;
import com.dawan.cinema.entities.Role;
import com.dawan.cinema.entities.User;
import com.dawan.cinema.exceptions.BusinessException;
import com.dawan.cinema.mappers.UserMapper;
import com.dawan.cinema.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepository, new UserMapper(), passwordEncoder);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private static void loggedInAs(String username) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(username, null, List.of()));
    }

    private static User admin(long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("HASH");
        user.setRole(Role.ADMIN);
        return user;
    }

    @Test
    void create_hache_le_mot_de_passe_et_attribue_le_role_admin() {
        when(passwordEncoder.encode("motdepasse123")).thenReturn("HASH");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UserDto dto = new UserDto(null, "alan", "Alan", "G", "alan@test.fr", null, "motdepasse123");

        UserDto result = service.create(dto);

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(saved.capture());
        assertThat(saved.getValue().getPassword()).isEqualTo("HASH");
        assertThat(saved.getValue().getRole()).isEqualTo(Role.ADMIN);
        assertThat(result.password()).isNull();
    }

    @Test
    void delete_refuse_de_supprimer_son_propre_compte() {
        loggedInAs("admin");
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin(1L, "admin")));

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("propre compte");
        verify(userRepository, never()).delete(any());
    }

    @Test
    void delete_refuse_de_supprimer_le_dernier_administrateur() {
        loggedInAs("bob");
        when(userRepository.findById(2L)).thenReturn(Optional.of(admin(2L, "alice")));
        when(userRepository.countByRole(Role.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> service.delete(2L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("dernier administrateur");
    }

    @Test
    void delete_supprime_un_autre_admin_s_il_en_reste_au_moins_un() {
        loggedInAs("bob");
        User alice = admin(2L, "alice");
        when(userRepository.findById(2L)).thenReturn(Optional.of(alice));
        when(userRepository.countByRole(Role.ADMIN)).thenReturn(2L);

        service.delete(2L);

        verify(userRepository).delete(alice);
    }

    @Test
    void changePassword_refuse_un_mot_de_passe_actuel_incorrect() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin(1L, "admin")));
        when(passwordEncoder.matches("faux", "HASH")).thenReturn(false);

        assertThatThrownBy(() -> service.changePassword("admin", "faux", "nouveau123"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("incorrect");
    }

    @Test
    void changePassword_enregistre_le_nouveau_mot_de_passe_hache() {
        User user = admin(1L, "admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("ancien", "HASH")).thenReturn(true);
        when(passwordEncoder.encode("nouveau123")).thenReturn("NOUVEAU_HASH");

        service.changePassword("admin", "ancien", "nouveau123");

        assertThat(user.getPassword()).isEqualTo("NOUVEAU_HASH");
    }
}