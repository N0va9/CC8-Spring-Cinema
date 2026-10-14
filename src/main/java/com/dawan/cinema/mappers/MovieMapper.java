package com.dawan.cinema.mappers;

import org.springframework.stereotype.Component;

import com.dawan.cinema.dtos.MovieDto;
import com.dawan.cinema.entities.Movie;

/**
 * Conversion Movie <-> MovieDto.
 */
@Component
public class MovieMapper implements Mapper<Movie, MovieDto> {

    @Override
    public MovieDto toDto(Movie movie) {
        return new MovieDto(movie.getId(), movie.getTitre(), movie.getAnnee(), movie.getGenre());
    }

    @Override
    public Movie toEntity(MovieDto dto) {
        Movie movie = new Movie();
        updateEntityFromDto(movie, dto);
        return movie;
    }

    @Override
    public void updateEntityFromDto(Movie movie, MovieDto dto) {
        movie.setTitre(dto.titre());
        movie.setAnnee(dto.annee());
        movie.setGenre(dto.genre());
    }
}