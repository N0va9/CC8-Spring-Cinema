package com.dawan.cinema.controllers.web.forms;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dawan.cinema.dtos.SessionDto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet du formulaire d'administration d'une séance.
 * Les dates suivent le format du champ HTML datetime-local.
 */
@Getter
@Setter
@NoArgsConstructor
public class SessionForm implements AdminForm<SessionDto> {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm";

    private Long id;

    @NotNull(message = "Le film est obligatoire")
    private Long movieId;

    @NotNull(message = "La salle est obligatoire")
    private Long roomId;

    @NotNull(message = "Le début est obligatoire")
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime heureDebut;

    @NotNull(message = "La fin est obligatoire")
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime heureFin;

    @NotNull(message = "Le prix est obligatoire")
    @PositiveOrZero(message = "Le prix ne peut pas être négatif")
    @Digits(integer = 4, fraction = 2, message = "Prix invalide (2 décimales maximum)")
    private BigDecimal price;

    /** Validation multi-champs : la fin doit être strictement après le début. */
    @AssertTrue(message = "L'heure de fin doit être après l'heure de début")
    public boolean isPlageHoraireValide() {
        return heureDebut == null || heureFin == null || heureFin.isAfter(heureDebut);
    }

    /** Préremplit le formulaire à partir d'une séance existante. */
    public static SessionForm from(SessionDto dto) {
        SessionForm form = new SessionForm();
        form.setId(dto.id());
        form.setMovieId(dto.movieId());
        form.setRoomId(dto.roomId());
        form.setHeureDebut(dto.heureDebut());
        form.setHeureFin(dto.heureFin());
        form.setPrice(dto.price());
        return form;
    }

    /** Les champs d'affichage (titre, numéro de salle, cinéma) sont inutiles en entrée : null. */
    @Override
    public SessionDto toDto() {
        return new SessionDto(id, movieId, null, roomId, null, null, heureDebut, heureFin, price);
    }
}