package com.dawan.cinema.controllers.web.forms;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.dawan.cinema.dtos.RoomDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet du formulaire d'administration d'une salle.
 */
@Getter
@Setter
@NoArgsConstructor
public class RoomForm implements AdminForm<RoomDto> {

    private Long id;

    @NotNull(message = "Le cinéma est obligatoire")
    private Long cinemaId;

    @NotNull(message = "Le numéro est obligatoire")
    @Positive(message = "Le numéro doit être positif")
    private Integer numero;

    @NotNull(message = "La capacité est obligatoire")
    @Positive(message = "La capacité doit être positive")
    private Integer capacite;

    @NotNull(message = "La date de construction est obligatoire")
    @PastOrPresent(message = "La date de construction ne peut pas être dans le futur")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateConstr;

    /** Préremplit le formulaire à partir d'une salle existante. */
    public static RoomForm from(RoomDto dto) {
        RoomForm form = new RoomForm();
        form.setId(dto.id());
        form.setCinemaId(dto.cinemaId());
        form.setNumero(dto.numero());
        form.setCapacite(dto.capacite());
        form.setDateConstr(dto.dateConstr());
        return form;
    }

    /** cinemaNom est un champ d'affichage, inutile en entrée : null. */
    @Override
    public RoomDto toDto() {
        return new RoomDto(id, cinemaId, null, numero, capacite, dateConstr);
    }
}