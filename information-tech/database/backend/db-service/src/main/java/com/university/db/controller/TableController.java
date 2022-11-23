package com.university.db.controller;

import com.university.db.dto.TableDto;
import com.university.db.dto.intersect.IntersectionTableDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.exception.ConflictException;
import com.university.db.exception.InvalidDataException;
import com.university.db.exception.NotFoundException;
import com.university.db.service.TableService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.university.db.utils.RequestUtils.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/db-service")
public class TableController {

    private final TableService tableService;
    private final String apiContext;

    public TableController(TableService tableService, @Value("${api.context}") String apiContext) {
        this.tableService = tableService;
        this.apiContext = apiContext;
    }

    @RequestMapping(value = "/databases/{id}/tables", method = RequestMethod.POST, produces = {"application/hal+json"})
    public ResponseEntity<?> create(@Valid @NotBlank @PathVariable("id") String databaseId,
                                    @Valid @RequestBody TableMetadataDto dto) {
        try {
            TableDto table = tableService.create(databaseId, dto);
            return ResponseEntity
                    .created(URI.create(String.format("%s/databases/%s/tables/%s",
                            apiContext, databaseId, table.getId())))
                    .body(CollectionModel.of(table, getLinks(table.getId(), databaseId)));
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @RequestMapping(value = "/tables/{id}", method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> findById(@Valid @NotBlank @PathVariable String id) {
        try {
            TableDto table = tableService.findById(id);
            return ResponseEntity.ok(CollectionModel.of(table, getLinks(id)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @RequestMapping(value = "/databases/{id}/tables", method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> findInDatabase(@Valid @NotBlank @PathVariable String id) {
        try {
            List<TableDto> tables = tableService.findInDatabase(id);
            tables.forEach(t -> t.add(getLinks(t.getId())));
            Link selfLink = linkTo(methodOn(TableController.class).findInDatabase(id)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(tables, selfLink));
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

    @RequestMapping(value = "/databases/{databaseId}/tables/{id}", method = RequestMethod.PUT,
            produces = {"application/hal+json"})
    public ResponseEntity<?> edit(@Valid @NotBlank @PathVariable String databaseId,
                                  @Valid @NotBlank @PathVariable String id,
                                  @Valid @RequestBody TableMetadataDto dto) {
        try {
            TableDto table = tableService.edit(databaseId, id, dto);
            return ResponseEntity.ok(CollectionModel.of(table, getLinks(table.getId(), databaseId)));
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        } catch (ConflictException e) {
            return conflict(e.getMessage());
        }
    }

    @RequestMapping(value = "/tables/intersect", method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> intersect(@Valid @NotBlank @RequestParam String leftId,
                                       @Valid @NotBlank @RequestParam String rightId) {
        try {
            IntersectionTableDto table = tableService.intersect(leftId, rightId);
            Link selfLink = linkTo(methodOn(TableController.class).intersect(leftId, rightId)).withSelfRel();
            Link rightLink = linkTo(methodOn(TableController.class).findById(rightId)).withRel("rightTable");
            Link leftLink = linkTo(methodOn(TableController.class).findById(leftId)).withRel("leftTable");
            return ResponseEntity.ok(CollectionModel.of(table, List.of(selfLink, rightLink, leftLink)));
        } catch (InvalidDataException e) {
            return badRequest(e.getMessage());
        } catch (NotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    private List<Link> getLinks(String id, String dbId) {
        List<Link> links = getLinks(id);
        links.add(linkTo(methodOn(TableController.class).findInDatabase(dbId)).withRel("tables"));
        return links;
    }

    private List<Link> getLinks(String id) {
        List<Link> result = new ArrayList<>();
        result.add(linkTo(methodOn(TableController.class)
                .findById(id)).withSelfRel());
        result.add(linkTo(methodOn(ColumnController.class)
                .findAll(id)).withRel("columns"));
        result.add(linkTo(methodOn(RowController.class)
                .findAll(id)).withRel("rows"));
        return result;
    }

}
