package com.university.db.service;

import com.university.db.dto.TableCreateDto;
import com.university.db.entity.Database;
import com.university.db.entity.Table;
import com.university.db.exception.ConflictException;
import com.university.db.repository.DatabaseRepository;
import com.university.db.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TableService {

    private final DatabaseRepository databaseRepository;
    private final TableRepository tableRepository;

    public TableService(DatabaseRepository databaseRepository,
                        TableRepository tableRepository) {
        this.databaseRepository = databaseRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public Table create(String databaseId, TableCreateDto dto) throws ConflictException {
        Optional<Database> databaseOpt = databaseRepository.findById(databaseId);
        Database database = databaseOpt.orElseThrow(() -> new ConflictException(
                String.format("Database with id %s does not exist", databaseId))
        );
        Optional<Table> tableOpt = tableRepository.findByName(dto.getName());
        if (tableOpt.isPresent()) {
            throw new ConflictException(String.format("Table with name %s already exists", dto.getName()));
        }
        Table table = new Table();
        table.setName(dto.getName());
        table = tableRepository.save(table);
        database.getTables().add(table);
        databaseRepository.save(database);
        return table;
    }

    public Optional<Table> findById(String id) {
        return tableRepository.findById(id);
    }

}
