package com.university.db.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.university.db.dto.DatabaseCreateDto;
import com.university.db.dto.DatabaseDto;
import com.university.db.dto.ErrorResponse;
import com.university.db.entity.Database;
import com.university.db.exception.ConflictException;
import com.university.db.mapper.DatabaseMapper;
import com.university.db.service.DatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> create(@RequestBody DatabaseCreateDto dto) {
        try {
            Database database = databaseService.create(dto.getName());
            return ResponseEntity.created(URI.create(apiContext + "/databases/" + database.getId()))
                    .body(DatabaseMapper.databaseToDatabaseDto(database));
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Database> database = databaseService.findById(id);
        if (database.isPresent()) {
            return ResponseEntity.ok(DatabaseMapper.databaseToDatabaseDto(database.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportDb(@RequestParam String id) throws JsonProcessingException {
        Optional<Database> database = databaseService.findById(id);
        if (database.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(databaseService.convertToFile(database.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<?> findByName(@RequestParam String name) {
        Optional<Database> database = databaseService.findByName(name);
        if (database.isPresent()) {
            return ResponseEntity.ok(DatabaseMapper.databaseToDatabaseDto(database.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Database> databases = databaseService.findAll();
        List<DatabaseDto> dto = databases.stream().map(DatabaseMapper::databaseToDatabaseDto).toList();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> importDb(@RequestBody ImportDbDto dto) {
        return null;
    }

}
