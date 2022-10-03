package com.university.db.controller;

import com.university.db.dto.TableDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.TableService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.conflict;
import static com.university.db.utils.RequestUtils.notFound;

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
    public ResponseEntity<?> create(@PathVariable("id") String databaseId, @RequestBody TableMetadataDto dto) {
        try {
            TableDto table = tableService.create(databaseId, dto);
            return ResponseEntity
                    .created(URI.create(String.format("%s/databases/%s/tables/%s",
                            apiContext, databaseId, table.getId())))
                    .body(table);
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/tables/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            TableDto table = tableService.findById(id);
            return ResponseEntity.ok(table);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/databases/{id}/tables")
    public ResponseEntity<?> findInDatabase(@PathVariable String id) {
        try {
            List<TableDto> tables = tableService.findInDatabase(id);
            return ResponseEntity.ok(tables);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @DeleteMapping("/databases/{databaseId}/tables/{id}")
    public ResponseEntity<?> delete(@PathVariable String databaseId, @PathVariable String id) {
        try {
            tableService.delete(databaseId, id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PutMapping("/databases/{databaseId}/tables/{id}")
    public ResponseEntity<?> edit(@PathVariable String databaseId,
                                  @PathVariable String id,
                                  @RequestBody TableMetadataDto dto) {
        try {
            TableDto table = tableService.edit(databaseId, id, dto);
            return ResponseEntity.ok(table);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

}
