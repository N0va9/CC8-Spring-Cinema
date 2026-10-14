package com.dawan.cinema.services;

import com.dawan.cinema.mappers.Mapper;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public abstract class CRUDServiceImpl<T, D, ID> implements CRUDService<T, D, ID> {

    protected final CrudRepository<T, ID> repository;
    protected final Mapper<T, D> mapper;

    public CRUDServiceImpl(CrudRepository<T, ID> repository, Mapper<T, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Iterable<T> getAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public D create(D dto) {
        T entity = mapper.toEntity(dto);
        T entitySaved = repository.save(entity);
        return mapper.toDto(entitySaved);
    }

    @Override
    public T update(ID id, D dto) {
        T toUpdate = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with id : " + id));

        T updated = mapper.updateEntityFromDto(toUpdate, dto);
        return repository.save(updated);
    }

    @Override
    public void delete(ID id) {
        if (existsById(id)) {
            throw new RuntimeException("Resource not found with id : " + id);
        }
        repository.deleteById(id);
    }
}
