package com.dawan.cinema.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dawan.cinema.entities.BaseEntity;
import com.dawan.cinema.exceptions.ResourceNotFoundException;
import com.dawan.cinema.mappers.Mapper;


/**
 * Implementation générique du CRUD 
 * 
 * @param <E> type de l'entité
 * @param <D> type du DTO
 */
@Transactional (readOnly = true)
public abstract class CRUDServiceImpl<E extends BaseEntity, D> implements CRUDService<D> {

    protected final JpaRepository<E, Long> repository;
    protected final Mapper<E, D> mapper;
    private final String resourceName;

    protected CRUDServiceImpl(JpaRepository<E, Long> repository, Mapper<E, D> mapper, Class<E> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.resourceName = entityClass.getSimpleName();
    }

    @Override 
    public Page<D> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public D getById(Long id) {
        return mapper.toDto(findEntityById(id));
    }

    @Override 
    @Transactional
    public D create(D dto) {
        E saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override 
    @Transactional 
    public D update(Long id, D dto) {
        E entity = findEntityById(id);
        mapper.updateEntityFromDto(entity, dto);
        return mapper.toDto(entity);
    }

    @Override 
    @Transactional 
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(resourceName, id);
        }
        repository.deleteById(id);
    }

    /** Récupère l'entité ou lève ResourceNotFoundException */
    protected E findEntityById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
        
    }

}
