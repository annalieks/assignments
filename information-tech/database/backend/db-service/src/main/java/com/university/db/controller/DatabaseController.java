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

@RestController
@RequestMapping("${api.context}/databases")
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

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DatabaseMetadataDto dto) {
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
    public ResponseEntity<?> find(@Valid @NotBlank @PathVariable String id) {
        try {
            DatabaseDto database = databaseService.findById(id);
            return ResponseEntity.ok(database);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/{id}/export")
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

    @PostMapping("/import")
    public ResponseEntity<?> importDb(@Valid @NotBlank @RequestParam("database") MultipartFile file) {
        try {
            DatabaseDto db = serializationService.importFile(file);
            return ResponseEntity.ok(db);
        } catch (FileFormatException e) {
            return badRequest(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findByName(@Valid @NotBlank @RequestParam(required = false) String name) {
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
    public ResponseEntity<?> edit(@Valid @NotBlank @PathVariable String id,
                                  @Valid @RequestBody DatabaseMetadataDto dto) {
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
    public ResponseEntity<?> delete(@Valid @NotBlank @PathVariable String id) {
        try {
            databaseService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

}
