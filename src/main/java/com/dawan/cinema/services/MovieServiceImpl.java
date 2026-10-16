package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dawan.cinema.dtos.MovieDto;
import com.dawan.cinema.entities.Movie;
import com.dawan.cinema.mappers.MovieMapper;
import com.dawan.cinema.repositories.MovieRepository;

/**
 * Implémentation du service des films.
 * La recherche porte sur le titre et le genre.
 */
@Service
public class MovieServiceImpl extends CRUDServiceImpl<Movie, MovieDto, MovieRepository> implements MovieService {

    public MovieServiceImpl(MovieRepository repository, MovieMapper mapper) {
        super(repository, mapper, Movie.class);
    }

    @Override
    protected Page<Movie> searchByKeyword(String keyword, Pageable pageable) {
        return repository.search(keyword, pageable);
    }
}