package com.dawan.cinema.mappers;

import org.springframework.stereotype.Component;

import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.entities.Cinema;

/*
* Conversion Cinema <-> CinemaDto
*/
@Component
public class CinemaMapper implements Mapper<Cinema, CinemaDto> {

	@Override
	public CinemaDto toDto(Cinema cinema) {
		return new CinemaDto(cinema.getId(), cinema.getNom(), cinema.getVille(),
				cinema.getRue(), cinema.getNumero());
	}

	@Override
	public Cinema toEntity(CinemaDto dto) {
		Cinema cinema = new Cinema();
		updateEntityFromDto(cinema, dto);
		return cinema;
	}

	@Override
	public void updateEntityFromDto(Cinema cinema, CinemaDto dto) {
		cinema.setNom(dto.nom());
		cinema.setVille(dto.ville());
		cinema.setRue(dto.rue());
		cinema.setNumero(dto.numero());
	}
}
