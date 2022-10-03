package com.university.db.service;

import com.university.db.dto.TableDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.entity.Database;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.TableMapper;
import com.university.db.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

        databaseService.save(database);
        tableRepository.delete(table);
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
