package com.university.db.controller;

import com.university.db.dto.ColumnDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.ColumnService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.conflict;
import static com.university.db.utils.RequestUtils.notFound;

@RestController
@RequestMapping("${api.context}")
public class ColumnController {

    private final ColumnService columnService;
    private final String apiContext;

    public ColumnController(ColumnService columnService,
                            @Value("${api.context}") String apiContext) {
        this.columnService = columnService;
        this.apiContext = apiContext;
    }

    @PostMapping("/tables/{tableId}/columns")
    public ResponseEntity<?> create(@PathVariable String tableId, @RequestBody ColumnMetadataDto dto) {
        try {
            ColumnDto column = columnService.create(tableId, dto);
            return ResponseEntity
                    .created(URI.create(String.format("%s/tables/%s/columns/%s",
                            apiContext, tableId, column.getId())))
                    .body(column);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    @GetMapping("/columns/{id}")
    public ResponseEntity<?> find(@PathVariable String id) {
        try {
            ColumnDto column = columnService.findById(id);
            return ResponseEntity.ok(column);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/tables/{tableId}/columns")
    public ResponseEntity<?> findAll(@PathVariable String tableId) {
        try {
            List<ColumnDto> columns = columnService.findAll(tableId);
            return ResponseEntity.ok(columns);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @DeleteMapping("/tables/{tableId}/columns/{id}")
    public ResponseEntity<?> delete(@PathVariable String tableId, @PathVariable String id) {
        try {
            columnService.delete(tableId, id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PutMapping("/tables/{tableId}/columns/{id}")
    public ResponseEntity<?> edit(@PathVariable String tableId,
                                  @PathVariable String id,
                                  @RequestBody ColumnMetadataDto dto) {
        try {
            ColumnDto column = columnService.edit(tableId, id, dto);
            return ResponseEntity.ok(column);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

}
