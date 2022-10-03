package com.university.db.mapper;

import com.university.db.dto.TableDto;
import com.university.db.dto.export.ExportedTableDto;
import com.university.db.entity.Table;

import java.util.stream.Collectors;

public class TableMapper {

    public static TableDto tableToTableDto(Table t) {
        TableDto dto = new TableDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        return dto;
    }

    public static ExportedTableDto tableToExportedTableDto(Table t) {
        ExportedTableDto dto = new ExportedTableDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setColumns(t.getColumns().stream()
                .map(ColumnMapper::columnToColumnDto).collect(Collectors.toList()));
        dto.setRows(t.getRows().stream()
                .map(RowMapper::rowToRowDto).collect(Collectors.toList()));
        return dto;
    }

}
