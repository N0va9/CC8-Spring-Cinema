package com.dawan.cinema.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record RoomDto(
	Long id,
	@NotNull Long cinemaId,
	String cinemaNom,
	@Positive int numero,
	@Positive int capacite,
	@NotNull @PastOrPresent LocalDate dateConstr
){

}
