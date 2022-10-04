package com.university.db.service;

import com.university.db.dto.TableDto;
import com.university.db.dto.intersect.IntersectionTableDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.dto.metadata.RowMetadataDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.dto.serialization.ExportTableDto;
import com.university.db.entity.Column;
import com.university.db.entity.Database;
import com.university.db.entity.Row;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.exception.InvalidDataException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.ColumnMapper;
import com.university.db.mapper.RowMapper;
import com.university.db.mapper.TableMapper;
import com.university.db.repository.TableRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final DatabaseService databaseService;
    private final TableRepository tableRepository;

    public TableService(DatabaseService databaseService,
                        TableRepository tableRepository) {
        this.databaseService = databaseService;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableDto create(String databaseId, TableMetadataDto dto) throws NotFoundException, ConflictException {
        Database database = databaseService.findDatabaseById(databaseId);
        if (findDuplicate(database, dto.getName()).isPresent()) {
            handleExistsByName(dto.getName());
        }
        Table table = new Table();
        table.setName(dto.getName());
        table = save(table);
        database.getTables().add(table);
        databaseService.save(database);
        return TableMapper.tableToTableDto(table);
    }

    public TableDto findById(String id) throws NotFoundException {
        Table table = findTableById(id);
        return TableMapper.tableToTableDto(table);
    }

    public Table findTableById(String id) throws NotFoundException {
        Optional<Table> table = tableRepository.findById(id);
        if (table.isEmpty()) {
            handleNotFoundById(id);
        }
        return table.get();
    }

    public List<TableDto> findInDatabase(String databaseId) throws NotFoundException {
        Database database = databaseService.findDatabaseById(databaseId);
        return database.getTables().stream()
                .map(TableMapper::tableToTableDto)
                .collect(Collectors.toList());
    }

    public void delete(String databaseId, String id) throws NotFoundException {
        Database database = databaseService.findDatabaseById(databaseId);
        Table table = findTableById(id);
        if (!isWithinDatabase(id, database)) {
            handleNotFoundById(id);
        }

        // delete table from the database
        database.setTables(database.getTables().stream()
                .filter(t -> !t.getId().equals(id))
                .collect(Collectors.toList()));

        tableRepository.delete(table);
        databaseService.save(database);
    }

    @Transactional
    public TableDto edit(String databaseId, String id, TableMetadataDto dto)
            throws NotFoundException, ConflictException {
        Database database = databaseService.findDatabaseById(databaseId);
        Table table = findTableById(id);
        if (!isWithinDatabase(id, database)) {
            handleNotFoundById(id);
        }
        Optional<Table> duplicatedTable = findDuplicate(database, dto.getName());
        // PUT must be idempotent
        if (duplicatedTable.isPresent() && !duplicatedTable.get().getId().equals(id)) {
            handleExistsByName(dto.getName());
        }
        for (Table t : database.getTables()) {
            if (t.getId().equals(id)) {
                t.setName(dto.getName());
            }
        }
        table.setName(dto.getName());
        databaseService.save(database);
        table = save(table);
        return TableMapper.tableToTableDto(table);
    }

    static class ColumnComparator implements Comparator<Pair<Column, Integer>> {
        @Override
        public int compare(Pair<Column, Integer> o1, Pair<Column, Integer> o2) {
            return o1.getKey().getName().compareTo(o2.getKey().getName());
        }
    }

    public IntersectionTableDto intersect(String leftId, String rightId) throws NotFoundException, InvalidDataException {
        Table leftTable = findTableById(leftId);
        Table rightTable = findTableById(rightId);

        IntersectionTableDto result = new IntersectionTableDto();

        List<Pair<Column, Integer>> leftCols = new ArrayList<>();
        List<Pair<Column, Integer>> rightCols = new ArrayList<>();
        for (int i = 0; i < leftTable.getColumns().size(); i++) {
            leftCols.add(Pair.of(leftTable.getColumns().get(i), i));
        }
        for (int i = 0; i < rightTable.getColumns().size(); i++) {
            rightCols.add(Pair.of(rightTable.getColumns().get(i), i));
        }
        leftCols.sort(new ColumnComparator());
        rightCols.sort(new ColumnComparator());

        verifyColumns(leftCols, rightCols);
        result.setColumns(extractColumns(leftCols));

        Map<Integer, Integer> leftPos = new HashMap<>();
        Map<Integer, Integer> rightPos = new HashMap<>();
        for (int i = 0; i < leftCols.size(); i++) {
            leftPos.put(leftCols.get(i).getValue(), i);
        }
        for (int i = 0; i < rightCols.size(); i++) {
            rightPos.put(rightCols.get(i).getValue(), i);
        }

        List<Row> leftRows = leftTable.getRows();
        List<Row> rightRows = rightTable.getRows();
        Set<List<String>> occurrences = new HashSet<>();
        for (Row r : leftRows) {
            String[] tmpFields = new String[r.getFields().size()];
            for (int j = 0; j < r.getFields().size(); j++) {
                int idx = leftPos.get(j);
                tmpFields[idx] = r.getFields().get(j);
            }
            occurrences.add(List.of(tmpFields));

        }
        for (Row r : rightRows) {
            String[] tmpFields = new String[r.getFields().size()];
            for (int j = 0; j < r.getFields().size(); j++) {
                int idx = rightPos.get(j);
                tmpFields[idx] = r.getFields().get(j);
            }
            List<String> tmpFieldsList = List.of(tmpFields);
            if (occurrences.contains(tmpFieldsList)) {
                result.getRows().add(composeRow(tmpFieldsList));
            }
        }

        return result;
    }

    private void verifyColumns(List<Pair<Column, Integer>> leftCols,
                               List<Pair<Column, Integer>> rightCols) throws InvalidDataException {
        if (leftCols.size() != rightCols.size()) {
            throw new InvalidDataException("Different number of columns for intersection");
        }

        for (int i = 0; i < leftCols.size(); i++) {
            if (!leftCols.get(i).getKey().equals(rightCols.get(i).getKey())) {
                throw new InvalidDataException("Columns for intersection must match");
            }
        }
    }

    private List<ColumnMetadataDto> extractColumns(List<Pair<Column, Integer>> cols) {
        return cols.stream()
                .map(c -> ColumnMapper.columnToColumnMetadataDto(c.getKey()))
                .collect(Collectors.toList());
    }

    private RowMetadataDto composeRow(List<String> fields) {
        RowMetadataDto dto = new RowMetadataDto();
        dto.setFields(fields);
        return dto;
    }

    public Table save(Table t) {
        return tableRepository.save(t);
    }

    private Optional<Table> findDuplicate(Database db, String name) {
        return db.getTables().stream().filter(t -> t.getName().equals(name)).findFirst();
    }

    private boolean isWithinDatabase(String id, Database database) {
        return database.getTables().stream().anyMatch(r -> r.getId().equals(id));
    }

    private void handleExistsByName(String name) throws ConflictException {
        throw new ConflictException(String.format("Table with name %s already exists within the database", name));
    }

    private void handleNotFoundById(String id) throws NotFoundException {
        throw new NotFoundException(String.format("Table with id %s does not exist", id));
    }

}
