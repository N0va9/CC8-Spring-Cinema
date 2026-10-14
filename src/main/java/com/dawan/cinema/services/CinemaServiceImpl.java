package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dawan.cinema.dtos.CinemaDto;
import com.dawan.cinema.entities.Cinema;
import com.dawan.cinema.mappers.CinemaMapper;
import com.dawan.cinema.repositories.CinemaRepository;

/**
 * Implémentation du service des cinémas.
 * La recherche porte sur le nom et la ville.
 */
@Service
public class CinemaServiceImpl extends CRUDServiceImpl<Cinema, CinemaDto, CinemaRepository>
        implements CinemaService {

    public CinemaServiceImpl(CinemaRepository repository, CinemaMapper mapper) {
        super(repository, mapper, Cinema.class);
    }

    @Override
    protected Page<Cinema> searchByKeyword(String keyword, Pageable pageable) {
        return repository.search(keyword, pageable);
    }
}