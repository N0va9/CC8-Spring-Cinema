package com.dawan.cinema.controllers.web.forms;

import com.dawan.cinema.dtos.MovieDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet du formulaire d'administration d'un film.
 */
@Getter
@Setter
@NoArgsConstructor
public class MovieForm implements AdminForm<MovieDto> {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotNull(message = "L'année est obligatoire")
    @Min(value = 1895, message = "L'année doit être postérieure à 1895")
    private Integer annee;

    @NotBlank(message = "Le genre est obligatoire")
    private String genre;

    /** Préremplit le formulaire à partir d'un film existant. */
    public static MovieForm from(MovieDto dto) {
        MovieForm form = new MovieForm();
        form.setId(dto.id());
        form.setTitre(dto.titre());
        form.setAnnee(dto.annee());
        form.setGenre(dto.genre());
        return form;
    }

    @Override
    public MovieDto toDto() {
        return new MovieDto(id, titre, annee, genre);
    }
}