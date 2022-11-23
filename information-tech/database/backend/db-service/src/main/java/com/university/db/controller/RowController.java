package com.university.db.controller;

import com.university.db.dto.RowDto;
import com.university.db.dto.metadata.RowMetadataDto;
import com.university.db.exception.InvalidDataException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.RowService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

import static com.university.db.utils.RequestUtils.badRequest;
import static com.university.db.utils.RequestUtils.notFound;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/db-service")
public class RowController {

    private final String apiContext;
    private final RowService rowService;

    public RowController(RowService rowService,
                         @Value("${api.context}") String apiContext) {
        this.apiContext = apiContext;
        this.rowService = rowService;
    }

    @GetMapping("/rows/{id}")
    public ResponseEntity<?> find(@Valid @NotBlank @PathVariable String id) {
        try {
            RowDto row = rowService.find(id);
            return ResponseEntity.ok(CollectionModel.of(row, getLinks(id)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @GetMapping("/tables/{tableId}/rows")
    public ResponseEntity<?> findAll(@Valid @NotBlank @PathVariable String tableId) {
        try {
            List<RowDto> rows = rowService.findAll(tableId);
            rows.forEach(row -> row.add(getLinks(row.getId(), tableId)));
            Link selfLink = linkTo(methodOn(RowController.class).findAll(tableId)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(rows, selfLink));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @PostMapping("/tables/{tableId}/rows")
    public ResponseEntity<?> create(@Valid @NotBlank @PathVariable String tableId,
                                    @Valid @RequestBody RowMetadataDto dto) {
        try {
            RowDto row = rowService.create(tableId, dto);
            return ResponseEntity.created(URI.create(
                            String.format("%s/tables/%s/rows/%s", apiContext, tableId, row.getId())))
                    .body(CollectionModel.of(row, getLinks(row.getId(), tableId)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (InvalidDataException e) {
            return badRequest(e.getMessage());
        }
    }

    @PutMapping("/tables/{tableId}/rows/{id}")
    public ResponseEntity<?> edit(@Valid @NotBlank @PathVariable String tableId,
                                  @Valid @NotBlank @PathVariable String id,
                                  @Valid @RequestBody RowMetadataDto dto) {
        try {
            RowDto row = rowService.edit(tableId, id, dto);
            return ResponseEntity.ok(CollectionModel.of(row, getLinks(id, tableId)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (InvalidDataException e) {
            return badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/tables/{tableId}/rows/{id}")
    public ResponseEntity<?> delete(@Valid @NotBlank @PathVariable String tableId,
                                    @Valid @NotBlank @PathVariable String id) {
        try {
            rowService.delete(tableId, id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    private List<Link> getLinks(String id, String tableId) {
        Link findByIdLink = linkTo(methodOn(RowController.class)
                .find(id)).withSelfRel();
        Link allLink = linkTo(methodOn(RowController.class).findAll(tableId)).withRel("rows");
        return List.of(findByIdLink, allLink);
    }

    private List<Link> getLinks(String id) {
        Link findByIdLink = linkTo(methodOn(RowController.class)
                .find(id)).withSelfRel();
        return List.of(findByIdLink);
    }

}
