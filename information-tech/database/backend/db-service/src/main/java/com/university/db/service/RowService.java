package com.university.db.service;

import com.university.db.dto.RowDto;
import com.university.db.entity.Row;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
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

    private boolean isWithinTable(String id, Table table) {
        return table.getRows().stream().anyMatch(r -> r.getId().equals(id));
    }

    private void handleNotFoundById(String id) throws NotFoundException {
        throw new NotFoundException(String.format("Row with id %s not found within table", id));
    }

}
