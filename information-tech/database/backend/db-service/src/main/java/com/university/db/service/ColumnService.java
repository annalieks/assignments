package com.university.db.service;

import com.university.db.dto.ColumnDto;
import com.university.db.dto.metadata.ColumnEditableMetadataDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.entity.Column;
import com.university.db.entity.Row;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.ColumnMapper;
import com.university.db.repository.ColumnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final RowService rowService;
    private final TableService tableService;

    public ColumnService(ColumnRepository columnRepository,
                         RowService rowService,
                         TableService tableService) {
        this.columnRepository = columnRepository;
        this.rowService = rowService;
        this.tableService = tableService;
    }

    @Transactional
    public ColumnDto create(String tableId, ColumnMetadataDto dto)
            throws NotFoundException, ConflictException {
        Table table = tableService.findTableById(tableId);
        if (findDuplicate(table, dto.getName()).isPresent()) {
            handleExistsByName(dto.getName());
        }
        for (Row r : table.getRows()) {
            r.getFields().add(null);
            rowService.save(r);
        }
        Column column = ColumnMapper.columnMetadataDtoToColumn(dto);
        column = save(column);
        table.getColumns().add(column);
        tableService.save(table);
        return ColumnMapper.columnToColumnDto(column);
    }

    public ColumnDto findById(String id) throws NotFoundException {
        Column column = findColumnById(id);
        return ColumnMapper.columnToColumnDto(column);
    }

    public Column findColumnById(String id) throws NotFoundException {
        Optional<Column> column = columnRepository.findById(id);
        if (column.isEmpty()) {
            handleNotFoundById(id);
        }
        return column.get();
    }

    public List<ColumnDto> findAll(String tableId) throws NotFoundException {
        Table table = tableService.findTableById(tableId);
        return table.getColumns().stream()
                .map(ColumnMapper::columnToColumnDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(String tableId, String id) throws NotFoundException {
        Table table = tableService.findTableById(tableId);
        Column column = findColumnById(id);
        List<Column> columns = table.getColumns();
        int idx = -1;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getId().equals(id)) {
                idx = i;
            }
        }
        if (idx == -1) {
            handleNotFoundById(id);
        }
        table.setColumns(table.getColumns().stream()
                .filter(c -> !c.getId().equals(id))
                .collect(Collectors.toList()));
        for (Row row : table.getRows()) {
            row.getFields().remove(idx);
            if (row.getFields().size() == 0) {
                rowService.delete(row);
            } else {
                rowService.save(row);
            }
        }
        table.setRows(table.getRows().stream()
                .filter(r -> r.getFields().size() != 0)
                .collect(Collectors.toList()));
        tableService.save(table);
        columnRepository.delete(column);
    }

    @Transactional
    public ColumnDto edit(String tableId, String id, ColumnEditableMetadataDto dto)
            throws NotFoundException, ConflictException {
        Table table = tableService.findTableById(tableId);
        Column column = findColumnById(id);
        if (!isWithinTable(id, table)) {
            handleNotFoundById(id);
        }
        Optional<Column> duplicatedColumn = findDuplicate(table, dto.getName());
        if (duplicatedColumn.isPresent() && !duplicatedColumn.get().getId().equals(id)) {
            handleExistsByName(dto.getName());
        }
        column.setName(dto.getName());
        for (Column c : table.getColumns()) {
            if (c.getId().equals(id)) {
                c.setName(dto.getName());
            }
        }
        tableService.save(table);
        column = save(column);
        return ColumnMapper.columnToColumnDto(column);
    }

    public Column save(Column c) {
        return columnRepository.save(c);
    }

    private boolean isWithinTable(String id, Table table) {
        return table.getColumns().stream().anyMatch(c -> c.getId().equals(id));
    }

    private void handleExistsByName(String name) throws ConflictException {
        throw new ConflictException(String.format("Column with name %s already exists within the table", name));
    }

    private void handleNotFoundById(String id) throws NotFoundException {
        throw new NotFoundException(String.format("Column with id %s not found", id));
    }

    private Optional<Column> findDuplicate(Table t, String name) {
        return t.getColumns().stream().filter(c -> c.getName().equals(name)).findFirst();
    }

}
