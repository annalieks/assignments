package com.university.db.controller;

import com.university.db.dto.DatabaseDto;
import com.university.db.dto.ErrorResponse;
import com.university.db.dto.metadata.DatabaseMetadataDto;
import com.university.db.dto.serialization.ExportDatabaseDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.FileFormatException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.DatabaseService;
import com.university.db.service.SerializationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/db-service/databases")
public class DatabaseController {

    private final DatabaseService databaseService;
    private final SerializationService serializationService;
    private final String apiContext;

    public DatabaseController(DatabaseService databaseService,
                              SerializationService serializationService,
                              @Value("${api.context}") String apiContext) {
        this.databaseService = databaseService;
        this.serializationService = serializationService;
        this.apiContext = apiContext;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = {"application/hal+json"})
    public ResponseEntity<?> create(@Valid @RequestBody DatabaseMetadataDto dto) {
        try {
            DatabaseDto database = databaseService.create(dto.getName());
            RepresentationModel<?> result = CollectionModel.of(database,
                    getLinks(database.getId(), database.getName()));
            return ResponseEntity
                    .created(URI.create(String.format("%s/databases/%s", apiContext, database.getId())))
                    .body(result);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> find(@Valid @NotBlank @PathVariable String id) {
        try {
            DatabaseDto database = databaseService.findById(id);
            RepresentationModel<?> result = CollectionModel.of(database,
                    getLinks(database.getId(), database.getName()));
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/export", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> exportDb(@Valid @NotBlank @PathVariable String id) {
        try {
            ExportDatabaseDto database = serializationService.export(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Content-Disposition",
                            "attachment; filename=" + database.getName() + ".json")
                    .body(database);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> importDb(@Valid @NotBlank @RequestParam("database") MultipartFile file) {
        try {
            DatabaseDto db = serializationService.importFile(file);
            RepresentationModel<?> result = CollectionModel.of(db,
                   getLinks(db.getId(), db.getName()));
            return ResponseEntity.ok(result);
        } catch (FileFormatException e) {
            return badRequest(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> findByName(@Valid @NotBlank @RequestParam(required = false) String name) {
        if (name == null) {
            List<DatabaseDto> databases = databaseService.findAll();
            databases.forEach(d -> d.add(getLinks(d.getId(), d.getName())));
            Link allDatabases = linkTo(methodOn(DatabaseController.class).findByName(null)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(databases, allDatabases));
        }
        try {
            DatabaseDto database = databaseService.findByName(name);
            RepresentationModel<?> result = CollectionModel.of(database,
                    getLinks(database.getId(), database.getName()));
            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {"application/hal+json"})
    public ResponseEntity<?> edit(@Valid @NotBlank @PathVariable String id,
                                  @Valid @RequestBody DatabaseMetadataDto dto) {
        try {
            DatabaseDto db = databaseService.edit(id, dto);
            return ResponseEntity.ok(CollectionModel.of(db, getLinks(db.getId(), db.getName())));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/hal+json"})
    public ResponseEntity<?> delete(@Valid @NotBlank @PathVariable String id) {
        try {
            databaseService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    private List<Link> getLinks(String id, String name) {
        Link findByIdLink = linkTo(methodOn(DatabaseController.class)
                .find(id)).withSelfRel();
        Link findByNameLink = linkTo(methodOn(DatabaseController.class)
                .findByName(name)).withSelfRel();

        Link tablesLink = linkTo(methodOn(TableController.class)
                .findInDatabase(id)).withRel("tables");
        Link findAllLink = linkTo(methodOn(DatabaseController.class)
                .findByName(null)).withRel("databases");
        Link exportLink = linkTo(methodOn(DatabaseController.class)
                .exportDb(id)).withRel("export");

        return List.of(findByIdLink, findByNameLink, tablesLink, findAllLink, exportLink);
    }

}
