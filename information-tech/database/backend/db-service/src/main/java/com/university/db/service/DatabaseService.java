package com.university.db.service;

import com.university.db.dto.DatabaseDto;
import com.university.db.dto.metadata.DatabaseMetadataDto;
import com.university.db.dto.export.ExportedDatabaseDto;
import com.university.db.entity.Database;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.mapper.DatabaseMapper;
import com.university.db.repository.ColumnRepository;
import com.university.db.repository.DatabaseRepository;
import com.university.db.repository.RowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final ColumnRepository columnRepository;
    private final RowRepository rowRepository;

    public DatabaseService(DatabaseRepository databaseRepository,
                           ColumnRepository columnRepository,
                           RowRepository rowRepository) {
        this.databaseRepository = databaseRepository;
        this.columnRepository = columnRepository;
        this.rowRepository = rowRepository;
    }

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

    public ExportedDatabaseDto export(String id) throws NotFoundException {
        Optional<Database> db = databaseRepository.findById(id);
        if (db.isEmpty()) {
            handleNotFoundById(id);
        }
        return DatabaseMapper.databaseToExportedDatabaseDto(db.get());
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

    public void delete(String id) throws NotFoundException {
        Optional<Database> existingDb = databaseRepository.findById(id);
        if (existingDb.isEmpty()) {
            handleNotFoundById(id);
        }
        Database db = existingDb.get();
        for (Table t : db.getTables()) {
            columnRepository.deleteAll(t.getColumns());
            rowRepository.deleteAll(t.getRows());
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
