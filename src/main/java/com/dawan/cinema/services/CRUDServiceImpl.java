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
 * @param <R> type du repository
 */
@Transactional (readOnly = true)
public abstract class CRUDServiceImpl<E extends BaseEntity, D, R extends JpaRepository<E, Long>>
        implements CRUDService<D> {

    protected final R repository;
    protected final Mapper<E, D> mapper;
    private final String resourceName;

    protected CRUDServiceImpl(R repository, Mapper<E, D> mapper, Class<E> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.resourceName = entityClass.getSimpleName();
    }

    @Override
    public Page<D> getAll(String keyword, Pageable pageable) {
        Page<E> page = (keyword == null || keyword.isBlank())
            ? repository.findAll(pageable)
            : searchByKeyword(keyword.trim(), pageable);
        return page.map(mapper::toDto);
    }

    @Override
    public D getById(Long id) {
        return mapper.toDto(findEntityById(id));
    }

    @Override
    @Transactional
    public D create(D dto) {
        E entity = mapper.toEntity(dto);
        resolveRelations(entity, dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public D update(Long id, D dto) {
        E entity = findEntityById(id);
        mapper.updateEntityFromDto(entity, dto);
        resolveRelations(entity, dto);
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

    /**
     * Recherche propre à chaque entité
     *
     * @param keyword  mot-clé non vide
     * @param pageable page, taille et tri demandés
     */
    protected abstract Page<E> searchByKeyword(String keyword, Pageable pageable);

    /**
     * Rattache les entités liées (ex. le cinéma d'une salle)
     * à partir des identifiants du DTO. Ne fait rien par défaut.
     *
     * @param entity entité à compléter
     * @param dto    DTO source
     */
    protected void resolveRelations(E entity, D dto) {
        // aucune relation par défaut
    }

}
