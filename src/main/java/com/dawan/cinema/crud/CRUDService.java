package com.dawan.cinema.crud;

import java.util.Optional;

public interface CRUDService<T, D, ID> {
    Iterable<T> getAll();
    Optional<T> getById(ID id);
    boolean existsById(ID id);
    D create (D dto);
    T update(ID id, D dto);
    void delete(ID id);
}
