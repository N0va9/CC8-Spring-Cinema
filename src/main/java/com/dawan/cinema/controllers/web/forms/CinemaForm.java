package com.dawan.cinema.controllers.web.forms;

import com.dawan.cinema.dtos.CinemaDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet du formulaire d'administration d'un cinéma (modifiable, compatible th:field).
 * Converti en CinemaDto pour appeler le service.
 */
@Getter
@Setter
@NoArgsConstructor
public class CinemaForm {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotBlank(message = "La rue est obligatoire")
    private String rue;

    @NotNull(message = "Le numéro est obligatoire")
    @Positive(message = "Le numéro doit être positif")
    private Integer numero;

    /** Préremplit le formulaire à partir d'un cinéma existant. */
    public static CinemaForm from(CinemaDto dto) {
        CinemaForm form = new CinemaForm();
        form.setId(dto.id());
        form.setNom(dto.nom());
        form.setVille(dto.ville());
        form.setRue(dto.rue());
        form.setNumero(dto.numero());
        return form;
    }

    /** Convertit le formulaire en DTO pour le service. */
    public CinemaDto toDto() {
        return new CinemaDto(id, nom, ville, rue, numero);
    }
}