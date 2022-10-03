package com.university.db.controller;

import com.university.db.dto.DatabaseDto;
import com.university.db.dto.metadata.DatabaseMetadataDto;
import com.university.db.dto.ErrorResponse;
import com.university.db.dto.export.ExportedDatabaseDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.DatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.conflict;
import static com.university.db.utils.RequestUtils.notFound;

@RestController
@RequestMapping("${api.context}/databases")
public class DatabaseController {

    private final DatabaseService databaseService;
    private final String apiContext;

    public DatabaseController(DatabaseService databaseService,
                              @Value("${api.context}") String apiContext) {
        this.databaseService = databaseService;
        this.apiContext = apiContext;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DatabaseMetadataDto dto) {
        try {
            DatabaseDto database = databaseService.create(dto.getName());
            return ResponseEntity
                    .created(URI.create(String.format("%s/databases/%s", apiContext, database.getId())))
                    .body(database);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable String id) {
        try {
            DatabaseDto database = databaseService.findById(id);
            return ResponseEntity.ok(database);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping(path = "/{id}/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> exportDb(@PathVariable String id) {
        try {
            ExportedDatabaseDto database = databaseService.export(id);
            return ResponseEntity.ok().body(database);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findByName(@RequestParam(required = false) String name) {
        if (name == null) {
            List<DatabaseDto> databases = databaseService.findAll();
            return ResponseEntity.ok(databases);
        }
        try {
            DatabaseDto database = databaseService.findByName(name);
            return ResponseEntity.ok(database);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable String id, @RequestBody DatabaseMetadataDto dto) {
        try {
            DatabaseDto db = databaseService.edit(id, dto);
            return ResponseEntity.ok(db);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            databaseService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

}
