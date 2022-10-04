package com.university.db.service;

import com.university.db.dto.DatabaseDto;
import com.university.db.dto.metadata.DatabaseMetadataDto;
import com.university.db.entity.Column;
import com.university.db.entity.Database;
import com.university.db.entity.Row;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.DatabaseMapper;
import com.university.db.repository.ColumnRepository;
import com.university.db.repository.DatabaseRepository;
import com.university.db.repository.RowRepository;
import com.university.db.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final TableRepository tableRepository;
    private final ColumnRepository columnRepository;
    private final RowRepository rowRepository;

    public DatabaseDto create(String name) throws ConflictException {
        Optional<Database> existingDb = databaseRepository.findByName(name);
        if (existingDb.isPresent()) {
            handleExistsByName(name);
        }
        Database db = new Database();
        db.setName(name);
        db = databaseRepository.save(db);
        return DatabaseMapper.databaseToDatabaseDto(db);
    }

    public DatabaseDto findById(String id) throws NotFoundException {
        Optional<Database> db = databaseRepository.findById(id);
        if (db.isEmpty()) {
            handleNotFoundById(id);
        }
        return DatabaseMapper.databaseToDatabaseDto(db.get());
    }

    public DatabaseDto findByName(String name) throws NotFoundException {
        Optional<Database> db = databaseRepository.findByName(name);
        if (db.isEmpty()) {
            handleNotFoundByName(name);
        }
        return DatabaseMapper.databaseToDatabaseDto(db.get());
    }

    public List<DatabaseDto> findAll() {
        List<Database> dbs = databaseRepository.findAll();
        return dbs.stream()
                .map(DatabaseMapper::databaseToDatabaseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DatabaseDto edit(String id, DatabaseMetadataDto dto)
            throws NotFoundException, ConflictException {
        Optional<Database> existingDb = databaseRepository.findById(id);
        if (existingDb.isEmpty()) {
            handleNotFoundById(id);
        }
        Database db = existingDb.get();
        if (db.getName().equals(dto.getName())) {
            handleExistsByName(dto.getName());
        }
        db.setName(dto.getName());
        db = databaseRepository.save(db);
        return DatabaseMapper.databaseToDatabaseDto(db);
    }

    @Transactional
    public void delete(String id) throws NotFoundException {
        Optional<Database> existingDb = databaseRepository.findById(id);
        if (existingDb.isEmpty()) {
            handleNotFoundById(id);
        }
        Database db = existingDb.get();
        for (Table t : db.getTables()) {
            Optional<Table> tableOpt = tableRepository.findById(t.getId());
            Table fullTable = tableOpt.orElseThrow(() ->
                    new NotFoundException(String.format("Table with id %s not found", t.getId())));
            columnRepository.deleteAll(fullTable.getColumns());
            rowRepository.deleteAll(fullTable.getRows());
            tableRepository.delete(t);
        }
        databaseRepository.delete(existingDb.get());
    }

    public Database findDatabaseById(String id) throws NotFoundException {
        Optional<Database> database = databaseRepository.findById(id);
        if (database.isEmpty()) {
            handleNotFoundById(id);
        }
        return database.get();
    }

    public Database save(Database database) {
        return databaseRepository.save(database);
    }

    private void handleNotFoundById(String id) throws NotFoundException {
        throw new NotFoundException(String.format("Database with id %s not found", id));
    }

    private void handleNotFoundByName(String name) throws NotFoundException {
        throw new NotFoundException(String.format("Database with name %s not found", name));
    }

    private void handleExistsByName(String name) throws ConflictException {
        throw new ConflictException(String.format("Database with name %s already exists", name));
    }
}
