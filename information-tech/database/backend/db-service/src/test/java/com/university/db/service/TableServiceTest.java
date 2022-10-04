package com.university.db.service;

import com.university.db.dto.intersect.IntersectionTableDto;
import com.university.db.entity.Column;
import com.university.db.entity.Row;
import com.university.db.entity.Table;
import com.university.db.entity.ValueType;
import com.university.db.exception.InvalidDataException;
import com.university.db.exception.NotFoundException;
import com.university.db.repository.TableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableServiceTest {

    TableService tableService;

    TableRepository tableRepository;
    DatabaseService databaseService;

    @BeforeEach
    public void init() {
        tableRepository = Mockito.mock(TableRepository.class);
        databaseService = Mockito.mock(DatabaseService.class);
        tableService = new TableService(databaseService, tableRepository);
    }

    @Test
    public void testIntersection_validColumns() throws InvalidDataException, NotFoundException {
        String id1 = "id1", id2 = "id2";
        Table table1 = createLeftTable();
        Table table2 = createRightTable();
        Mockito.when(tableRepository.findById(id1)).thenReturn(Optional.of(table1));
        Mockito.when(tableRepository.findById(id2)).thenReturn(Optional.of(table2));

        IntersectionTableDto result = tableService.intersect(id1, id2);
        Assertions.assertEquals(2, result.getColumns().size());
        Assertions.assertEquals("Column1", result.getColumns().get(0).getName());
        Assertions.assertEquals("Column2", result.getColumns().get(1).getName());
        Assertions.assertEquals(1, result.getRows().size());
        Assertions.assertEquals(List.of("2", "c"), result.getRows().get(0).getFields());
    }

    @Test
    public void testIntersection_sameData() throws InvalidDataException, NotFoundException {
        String id1 = "id1", id2 = "id2";
        Table table1 = createLeftTable();
        Table table2 = createLeftTable();
        Mockito.when(tableRepository.findById(id1)).thenReturn(Optional.of(table1));
        Mockito.when(tableRepository.findById(id2)).thenReturn(Optional.of(table2));

        IntersectionTableDto result = tableService.intersect(id1, id2);
        Assertions.assertEquals(2, result.getColumns().size());
        Assertions.assertEquals("Column1", result.getColumns().get(0).getName());
        Assertions.assertEquals("Column2", result.getColumns().get(1).getName());
        Assertions.assertEquals(2, result.getRows().size());
        Assertions.assertEquals(List.of("1", "a"), result.getRows().get(0).getFields());
        Assertions.assertEquals(List.of("2", "c"), result.getRows().get(1).getFields());
    }

    @Test
    public void testIntersection_differentData() throws InvalidDataException, NotFoundException {
        String id1 = "id1", id2 = "id2";
        Table table1 = createLeftTable();
        Table table2 = createDifferentDataTable();
        Mockito.when(tableRepository.findById(id1)).thenReturn(Optional.of(table1));
        Mockito.when(tableRepository.findById(id2)).thenReturn(Optional.of(table2));

        IntersectionTableDto result = tableService.intersect(id1, id2);
        Assertions.assertEquals(2, result.getColumns().size());
        Assertions.assertEquals("Column1", result.getColumns().get(0).getName());
        Assertions.assertEquals("Column2", result.getColumns().get(1).getName());
        Assertions.assertEquals(0, result.getRows().size());
    }

    @Test
    public void testIntersection_differentColumns() {
        String id1 = "id1", id2 = "id2";
        Table table1 = createLeftTable();
        Table table2 = createDifferentColumnsTable();
        Mockito.when(tableRepository.findById(id1)).thenReturn(Optional.of(table1));
        Mockito.when(tableRepository.findById(id2)).thenReturn(Optional.of(table2));

        Assertions.assertThrows(InvalidDataException.class, () -> tableService.intersect(id1, id2));
    }

    private Table createLeftTable() {
        Table table = new Table();
        table.setName("Left table");
        table.setColumns(List.of(
                createColumn(2, ValueType.STRING),
                createColumn(1, ValueType.INTEGER)));
        table.setRows(List.of(createRow("a", "1"), createRow("c", "2")));
        return table;
    }

    private Table createRightTable() {
        Table table = new Table();
        table.setName("Right table");
        table.setColumns(List.of(
                createColumn(1, ValueType.INTEGER),
                createColumn(2, ValueType.STRING)));
        table.setRows(List.of(createRow("2", "c"), createRow("1", "a1")));
        return table;
    }

    private Table createDifferentDataTable() {
        Table table = new Table();
        table.setName("Different data table");
        table.setColumns(List.of(
                createColumn(1, ValueType.INTEGER),
                createColumn(2, ValueType.STRING)));
        table.setRows(List.of(createRow("11", "2"), createRow("3", "4")));
        return table;
    }

    private Table createDifferentColumnsTable() {
        Table table = new Table();
        table.setName("Different columns table");
        table.setColumns(List.of(
                createColumn(1, ValueType.INTEGER),
                createColumn(2, ValueType.INTEGER)));
        table.setRows(List.of(createRow("1", "2"), createRow("3", "4")));
        return table;
    }

    private Column createColumn(int id, ValueType type) {
        Column column = new Column();
        column.setName("Column" + id);
        column.setType(type);
        return column;
    }

    private Row createRow(String... args) {
        Row row = new Row();
        row.setFields(new ArrayList<>());
        for (String arg : args) {
            row.getFields().add(arg);
        }
        return row;
    }
}
