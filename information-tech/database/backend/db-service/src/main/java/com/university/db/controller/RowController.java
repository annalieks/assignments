package com.university.db.controller;

import com.university.db.dto.RowDto;
import com.university.db.dto.metadata.RowMetadataDto;
import com.university.db.exception.NotFoundException;
import com.university.db.service.RowService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.university.db.utils.RequestUtils.notFound;

@RestController
@RequestMapping("${api.context}")
public class RowController {

    private final String apiContext;
    private final RowService rowService;

    public RowController(RowService rowService,
                         @Value("${api.context}") String apiContext) {
        this.apiContext = apiContext;
        this.rowService = rowService;
    }

    @GetMapping("/rows/{id}")
    public ResponseEntity<?> find(@PathVariable String id) {
        try {
            RowDto row = rowService.find(id);
            return ResponseEntity.ok(row);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/tables/{tableId}/rows")
    public ResponseEntity<?> findAll(@PathVariable String tableId) {
        try {
            List<RowDto> rows = rowService.findAll(tableId);
            return ResponseEntity.ok(rows);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PostMapping("/tables/{tableId}/rows")
    public ResponseEntity<?> create(@PathVariable String tableId, RowMetadataDto dto) {
        return null;
    }

    @DeleteMapping("/tables/{tableId}/rows/{id}")
    public ResponseEntity<?> delete(@PathVariable String tableId, @PathVariable String id) {
        try {
            rowService.delete(tableId, id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

}
