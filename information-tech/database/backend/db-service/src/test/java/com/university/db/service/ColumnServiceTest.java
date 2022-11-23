package com.university.db.service;

import com.university.db.dto.ColumnDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.entity.Column;
import com.university.db.entity.Table;
import com.university.db.entity.ValueType;
import com.university.db.exception.ConflictException;
import com.university.db.exception.NotFoundException;
import com.university.db.repository.ColumnRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ColumnServiceTest {

    private ColumnRepository columnRepository;
    private RowService rowService;
    private TableService tableService;

    private ColumnService columnService;

    @BeforeEach
    public void init() {
        columnRepository = mock(ColumnRepository.class);
        rowService = mock(RowService.class);
        tableService = mock(TableService.class);

        columnService = new ColumnService(columnRepository, rowService, tableService);
    }

    @Test
    public void testCreateColumn_success() throws ConflictException, NotFoundException {
        String tableId = "tableId";
        ColumnMetadataDto columnMetadataDto = new ColumnMetadataDto();
        columnMetadataDto.setName("Column1");
        columnMetadataDto.setType(ValueType.STRING);
        Table table = new Table();
        Column column = new Column();
        column.setName(columnMetadataDto.getName());
        column.setType(columnMetadataDto.getType());

        when(tableService.findTableById(tableId)).thenReturn(table);
        when(columnRepository.save(any(Column.class))).thenReturn(column);
        ColumnDto columnDto = columnService.create(tableId, columnMetadataDto);
        Assertions.assertEquals(columnDto.getId(), column.getId());
        Assertions.assertEquals(columnDto.getName(), column.getName());
        verify(columnRepository, times(1)).save(any(Column.class));
    }

}
