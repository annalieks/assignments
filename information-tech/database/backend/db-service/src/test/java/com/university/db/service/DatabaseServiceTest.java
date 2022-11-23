package com.university.db.service;

import com.university.db.dto.DatabaseDto;
import com.university.db.entity.Database;
import com.university.db.exception.ConflictException;
import com.university.db.repository.ColumnRepository;
import com.university.db.repository.DatabaseRepository;
import com.university.db.repository.RowRepository;
import com.university.db.repository.TableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class DatabaseServiceTest {

    private DatabaseRepository databaseRepository;
    private TableRepository tableRepository;
    private ColumnRepository columnRepository;
    private RowRepository rowRepository;

    private DatabaseService databaseService;

    @BeforeEach
    public void init() {
        databaseRepository = mock(DatabaseRepository.class);
        tableRepository = mock(TableRepository.class);
        columnRepository = mock(ColumnRepository.class);
        rowRepository = mock(RowRepository.class);

        databaseService = new DatabaseService(databaseRepository,
                tableRepository, columnRepository, rowRepository);
    }

    @Test
    public void testCreateDatabase_success() throws ConflictException {
        String databaseName = "db";
        Database db = new Database();
        db.setName(databaseName);
        db.setId("id");
        when(databaseRepository.findByName(databaseName)).thenReturn(Optional.empty());
        when(databaseRepository.save(any(Database.class))).thenReturn(db);
        DatabaseDto databaseDto = databaseService.create(databaseName);
        verify(databaseRepository, times(1)).save(any(Database.class));
        Assertions.assertEquals(databaseDto.getName(), databaseName);
        Assertions.assertEquals(databaseDto.getId(), db.getId());
    }

    @Test
    public void testCreateDatabase_duplicateNotAllowed() {
        String databaseName = "db";
        Database db = new Database();
        when(databaseRepository.findByName(databaseName)).thenReturn(Optional.of(db));
        Assertions.assertThrows(ConflictException.class,
                () -> databaseService.create(databaseName));
    }

}
