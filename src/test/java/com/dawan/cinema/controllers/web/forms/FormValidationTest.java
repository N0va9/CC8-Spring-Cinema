package com.dawan.cinema.controllers.web.forms;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Tests des validations multi-champs des formulaires (@AssertTrue).
 */
class FormValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        factory.close();
    }

    /** Noms des propriétés en erreur. */
    private static Set<String> invalidFields(Object form) {
        return validator.validate(form).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toSet());
    }

    private static SessionForm validSessionForm() {
        SessionForm form = new SessionForm();
        form.setMovieId(1L);
        form.setRoomId(1L);
        form.setHeureDebut(LocalDateTime.of(2026, 10, 20, 14, 0));
        form.setHeureFin(LocalDateTime.of(2026, 10, 20, 16, 30));
        form.setPrice(new BigDecimal("9.50"));
        return form;
    }

    private static UserForm validUserForm() {
        UserForm form = new UserForm();
        form.setUsername("chris");
        form.setFirstName("chris");
        form.setLastName("H");
        form.setEmail("chris@test.fr");
        form.setPassword("motdepasse123");
        form.setConfirmPassword("motdepasse123");
        return form;
    }

    @Test
    void seance_valide_n_a_aucune_erreur() {
        assertThat(invalidFields(validSessionForm())).isEmpty();
    }

    @Test
    void seance_dont_la_fin_precede_le_debut_est_refusee() {
        SessionForm form = validSessionForm();
        form.setHeureFin(form.getHeureDebut().minusHours(1));

        assertThat(invalidFields(form)).containsExactly("plageHoraireValide");
    }

    @Test
    void utilisateur_sans_mot_de_passe_est_refuse_a_la_creation() {
        UserForm form = validUserForm();
        form.setPassword("");
        form.setConfirmPassword("");

        assertThat(invalidFields(form)).containsExactly("passwordValide");
    }

    @Test
    void utilisateur_sans_mot_de_passe_est_accepte_en_modification() {
        UserForm form = validUserForm();
        form.setId(1L);
        form.setPassword("");
        form.setConfirmPassword("");

        assertThat(invalidFields(form)).isEmpty();
    }

    @Test
    void utilisateur_avec_confirmation_differente_est_refuse() {
        UserForm form = validUserForm();
        form.setConfirmPassword("autrechose");

        assertThat(invalidFields(form)).containsExactly("confirmationValide");
    }

    @Test
    void mot_de_passe_trop_court_est_refuse() {
        UserForm form = validUserForm();
        form.setPassword("court");
        form.setConfirmPassword("court");

        assertThat(invalidFields(form)).containsExactly("passwordValide");
    }
}