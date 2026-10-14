package com.dawan.cinema.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.dawan.cinema.services.CRUDService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

public abstract class CRUDController<D> {

	protected final CRUDService<D> service;

	protected CRUDController(CRUDService<D> service) {
		this.service = service;
	}

	@GetMapping
	@Operation(summary = "Liste paginée")
	public Page<D> getAll(@ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
		return service.getAll(pageable);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Détail par id")
	public D getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Création")
	public D create(@Valid @RequestBody D dto) {
		return service.create(dto);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Modification")
	public D update(@PathVariable Long id, @Valid @RequestBody D dto) {
		return service.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Suppression")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}