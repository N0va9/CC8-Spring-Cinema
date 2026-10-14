package com.dawan.cinema.services;

import org.springframework.stereotype.Service;

import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.mappers.CinemaMapper;
import com.dawan.cinema.repositories.CinemaRepository;

@Service
public class CinemaServiceImpl extends CRUDServiceImpl<Cinema, CinemaDto, Long> {
	public CinemaServiceImpl(CinemaRepository repository, CinemaMapper mapper) {
		super(repository, mapper);
	}
}
