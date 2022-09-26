package com.university.db.controller;

import com.university.db.dto.ErrorResponse;
import com.university.db.dto.TableCreateDto;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.mapper.TableMapper;
import com.university.db.service.TableService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("${api.context}")
public class TableController {

    private final TableService tableService;
    private final String apiContext;

    public TableController(TableService tableService, @Value("${api.context}") String apiContext) {
        this.tableService = tableService;
        this.apiContext = apiContext;
    }

    @PostMapping("/databases/{id}/tables")
    public ResponseEntity<?> create(@PathVariable("id") String databaseId, @RequestBody TableCreateDto dto) {
        try {
            Table table = tableService.create(databaseId, dto);
            return ResponseEntity.created(URI.create(apiContext + "/databases/tables/" + table.getId()))
                    .body(TableMapper.tableToTableDto(table));
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/tables/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Table> table = tableService.findById(id);
        if (table.isPresent()) {
            return ResponseEntity.ok(TableMapper.tableToTableDto(table.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
