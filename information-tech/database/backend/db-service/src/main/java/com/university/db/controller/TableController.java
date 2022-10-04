package com.university.db.controller;

import com.university.db.dto.TableDto;
import com.university.db.dto.intersect.IntersectionTableDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.dto.serialization.ExportTableDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.InvalidDataException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.TableService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.*;

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
    public ResponseEntity<?> create(@Valid @NotBlank @PathVariable("id") String databaseId,
                                    @Valid @RequestBody TableMetadataDto dto) {
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
    public ResponseEntity<?> findById(@Valid @NotBlank @PathVariable String id) {
        try {
            TableDto table = tableService.findById(id);
            return ResponseEntity.ok(table);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/databases/{id}/tables")
    public ResponseEntity<?> findInDatabase(@Valid @NotBlank @PathVariable String id) {
        try {
            List<TableDto> tables = tableService.findInDatabase(id);
            return ResponseEntity.ok(tables);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @DeleteMapping("/databases/{databaseId}/tables/{id}")
    public ResponseEntity<?> delete(@Valid @NotBlank @PathVariable String databaseId,
                                    @Valid @NotBlank @PathVariable String id) {
        try {
            tableService.delete(databaseId, id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PutMapping("/databases/{databaseId}/tables/{id}")
    public ResponseEntity<?> edit(@Valid @NotBlank @PathVariable String databaseId,
                                  @Valid @NotBlank @PathVariable String id,
                                  @Valid @RequestBody TableMetadataDto dto) {
        try {
            TableDto table = tableService.edit(databaseId, id, dto);
            return ResponseEntity.ok(table);
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    @GetMapping("/tables/intersect")
    public ResponseEntity<?> intersect(@Valid @NotBlank @RequestParam String leftId,
                                       @Valid @NotBlank @RequestParam String rightId) {
        try {
            IntersectionTableDto table = tableService.intersect(leftId, rightId);
            return ResponseEntity.ok(table);
        } catch (InvalidDataException e) {
            return badRequest(e.getMessage());
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

}
