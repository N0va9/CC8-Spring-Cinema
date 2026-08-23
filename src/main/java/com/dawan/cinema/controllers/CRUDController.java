package com.dawan.cinema.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dawan.cinema.services.CRUDService;

import java.util.Optional;

// T entity D dto ID id
public abstract class CRUDController<T, D, ID> {
    protected final CRUDService<T, D, ID> service;

    protected CRUDController(CRUDService<T, D, ID> service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Create new [entity]",
            description = "Create and persist a new instance of [entity]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "[Entity] successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @SecurityRequirement(name="basicAuth")
    public D create(@ParameterObject @ModelAttribute D dto) {
        return service.create(dto);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get a [entity] by ID",
            description = "Retrieve a single [entity] by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "[Entity] found"),
                    @ApiResponse(responseCode = "404", description = "[Entity] not found")
            }
    )
    public ResponseEntity<T> getById(@PathVariable ID id) {
        Optional<T> optionalEntity = service.getById(id);
        return optionalEntity.map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/all")
    @Operation(
            summary = "Get all [entity]s",
            description = "Retrieve all [entity]s",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All [entity]s found"),
                    @ApiResponse(responseCode = "404", description = "[Entity]s not found")
            }
    )
    public Iterable<T> getAll() {
        return service.getAll();
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update a [entity] by ID",
            description = "Update the attributes of an existing [entity] identified by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "[Entity] successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid update data"),
                    @ApiResponse(responseCode = "404", description = "[Entity] not found")
            }
    )
    @SecurityRequirement(name="basicAuth")
    public ResponseEntity<T> update(@PathVariable ID id, @ParameterObject @ModelAttribute D dto) {
        T entity = service.update(id, dto);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a [entity] by ID",
            description = "Remove an existing [entity] permanently from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "[Entity] successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "[Entity] not found")
            }
    )
    @SecurityRequirement(name="basicAuth")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (service.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
