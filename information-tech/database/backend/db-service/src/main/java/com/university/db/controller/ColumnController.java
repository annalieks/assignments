package com.university.db.controller;

import com.university.db.dto.ColumnDto;
import com.university.db.dto.metadata.ColumnEditableMetadataDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.ColumnService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.conflict;
import static com.university.db.utils.RequestUtils.notFound;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/db-service")
public class ColumnController {

    private final ColumnService columnService;
    private final String apiContext;

    public ColumnController(ColumnService columnService,
                            @Value("${api.context}") String apiContext) {
        this.columnService = columnService;
        this.apiContext = apiContext;
    }

    @PostMapping("/tables/{tableId}/columns")
    public ResponseEntity<?> create(@Valid @NotBlank @PathVariable String tableId,
                                    @Valid @RequestBody ColumnMetadataDto dto) {
        try {
            ColumnDto column = columnService.create(tableId, dto);
            return ResponseEntity
                    .created(URI.create(String.format("%s/tables/%s/columns/%s",
                            apiContext, tableId, column.getId())))
                    .body(CollectionModel.of(column, getLinks(column.getId(), tableId)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    @GetMapping("/columns/{id}")
    public ResponseEntity<?> find(@Valid @NotBlank @PathVariable String id) {
        try {
            ColumnDto column = columnService.findById(id);
            return ResponseEntity.ok(CollectionModel.of(column, getLinks(id)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/tables/{tableId}/columns")
    public ResponseEntity<?> findAll(@Valid @NotBlank @PathVariable String tableId) {
        try {
            List<ColumnDto> columns = columnService.findAll(tableId);
            Link selfLink = linkTo(methodOn(ColumnController.class).findAll(tableId)).withSelfRel();
            columns.forEach(c -> c.add(getLinks(c.getId(), tableId)));
            return ResponseEntity.ok(CollectionModel.of(columns, selfLink));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @DeleteMapping("/tables/{tableId}/columns/{id}")
    public ResponseEntity<?> delete(@Valid @NotBlank @PathVariable String tableId,
                                    @Valid @NotBlank @PathVariable String id) {
        try {
            columnService.delete(tableId, id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PutMapping("/tables/{tableId}/columns/{id}")
    public ResponseEntity<?> edit(@Valid @NotBlank @PathVariable String tableId,
                                  @Valid @NotBlank @PathVariable String id,
                                  @Valid @RequestBody ColumnEditableMetadataDto dto) {
        try {
            ColumnDto column = columnService.edit(tableId, id, dto);
            return ResponseEntity.ok(CollectionModel.of(column, getLinks(id, tableId)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    private List<Link> getLinks(String id, String tableId) {
        Link findByIdLink = linkTo(methodOn(ColumnController.class)
                .find(id)).withSelfRel();
        Link allLink = linkTo(methodOn(ColumnController.class).findAll(tableId)).withRel("columns");
        return List.of(findByIdLink, allLink);
    }

    private List<Link> getLinks(String id) {
        Link findByIdLink = linkTo(methodOn(ColumnController.class)
                .find(id)).withSelfRel();
        return List.of(findByIdLink);
    }

}
