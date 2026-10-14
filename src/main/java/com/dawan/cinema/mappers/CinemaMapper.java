package com.dawan.cinema.mappers;

import org.springframework.stereotype.Component;

import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.entities.Cinema;

@Component
public class CinemaMapper implements Mapper<Cinema, CinemaDto>{
	
	@Override
	public Cinema toEntity(CinemaDto dto) {
		Cinema cinema = new Cinema();
		cinema.setNom(dto.getNom());
		cinema.setVille(dto.getVille());
		cinema.setRue(dto.getRue());
		cinema.setNumero(dto.getNumero());

		return cinema;
    }

	@Override
    public CinemaDto toDto(Cinema cinema) {
    	CinemaDto dto = new CinemaDto();
		dto.setNom(cinema.getNom());
		dto.setVille(cinema.getVille());
		dto.setRue(cinema.getRue());
		dto.setNumero(cinema.getNumero());

		return dto;
	}

	@Override
	public Cinema updateEntityFromDto(Cinema cinema,CinemaDto dto) {
		cinema.setNom(dto.getNom());
		cinema.setVille(dto.getVille());
		cinema.setRue(dto.getRue());
		cinema.setNumero(dto.getNumero());

		return cinema;
    }
}
