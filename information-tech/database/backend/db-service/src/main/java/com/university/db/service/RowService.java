package com.university.db.service;

import com.university.db.dto.RowDto;
import com.university.db.dto.metadata.RowMetadataDto;
import com.university.db.entity.Column;
import com.university.db.entity.Row;
import com.university.db.entity.Table;
import com.university.db.entity.ValueType;
import com.university.db.exception.InvalidDataException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.RowMapper;
import com.university.db.repository.RowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RowService {

    private final RowRepository rowRepository;
    private final TableService tableService;

    public RowService(RowRepository rowRepository, TableService tableService) {
        this.rowRepository = rowRepository;
        this.tableService = tableService;
    }

    public RowDto find(String id) throws NotFoundException {
        Row row = findRowById(id);
        return RowMapper.rowToRowDto(row);
    }

    public Row findRowById(String id) throws NotFoundException {
        Optional<Row> row = rowRepository.findById(id);
        if (row.isEmpty()) {
            handleNotFoundById(id);
        }
        return row.get();
    }

    public List<RowDto> findAll(String tableId) throws NotFoundException {
        Table table = tableService.findTableById(tableId);
        return table.getRows().stream()
                .map(RowMapper::rowToRowDto)
                .collect(Collectors.toList());
    }

    public RowDto create(String tableId, RowMetadataDto dto)
            throws NotFoundException, InvalidDataException {
        Table table = tableService.findTableById(tableId);
        validateFields(dto.getFields(), table.getColumns());
        Row row = new Row();
        row.setFields(dto.getFields());
        row = rowRepository.save(row);
        table.getRows().add(row);
        tableService.save(table);
        return RowMapper.rowToRowDto(row);
    }

    @Transactional
    public RowDto edit(String tableId, String rowId, RowMetadataDto dto) throws NotFoundException, InvalidDataException {
        Table table = tableService.findTableById(tableId);
        Row row = findRowById(rowId);
        validateFields(dto.getFields(), table.getColumns());
        row.setFields(dto.getFields());
        for (Row r : table.getRows()) {
            if (r.getId().equals(rowId)) {
                r.setFields(dto.getFields());
            }
        }
        tableService.save(table);
        row = rowRepository.save(row);
        return RowMapper.rowToRowDto(row);
    }

    public static void validateFields(List<String> fields, List<Column> columns) throws InvalidDataException {
        if (fields.size() != columns.size()) {
            handleInvalidData("number of fields is not equal to number of columns");
        }
        if (fields.size() == 0) {
            handleInvalidData("fields cannot be empty");
        }
        for (int i = 0; i < columns.size(); i++) {
            Column c = columns.get(i);
            String value = fields.get(i);
            if (!ValueType.validate(c.getType(), value)) {
                handleInvalidData(String.format("value %s is not of type %s", value, c.getType()));
            }
        }
    }

    @Transactional
    public void delete(String tableId, String id) throws NotFoundException {
        Table table = tableService.findTableById(tableId);
        Row row = findRowById(id);
        if (!isWithinTable(id, table)) {
            handleNotFoundById(id);
        }
        table.setRows(table.getRows().stream()
                .filter(r -> !r.getId().equals(id)).collect(Collectors.toList()));
        rowRepository.delete(row);
        tableService.save(table);
    }

    public Row save(Row row) {
        return rowRepository.save(row);
    }

    public void delete(Row row) {
        rowRepository.delete(row);
    }

    private boolean isWithinTable(String id, Table table) {
        return table.getRows().stream().anyMatch(r -> r.getId().equals(id));
    }

    private static void handleNotFoundById(String id) throws NotFoundException {
        throw new NotFoundException(String.format("Row with id %s not found within table", id));
    }

    private static void handleInvalidData(String reason) throws InvalidDataException {
        throw new InvalidDataException(String.format("Invalid data provided: %s", reason));
    }

}
