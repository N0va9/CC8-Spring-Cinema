package com.dawan.cinema.mappers;

public interface Mapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);

    default E updateEntityFromDto(E entity, D dto) {
        throw new UnsupportedOperationException("updateEntityFromDto not implemented");
    }
}