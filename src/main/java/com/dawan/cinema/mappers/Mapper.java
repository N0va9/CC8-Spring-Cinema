package com.dawan.cinema.mappers;

/**
 * Conversion entre un entité et son DTO
 *
 * @param <E> type de l'entité
 * @param <D> type du DTO
 */
public interface Mapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);

    void updateEntityFromDto(E entity, D dto);
}