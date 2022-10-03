package com.university.db.mapper;

import com.university.db.dto.DatabaseDto;
import com.university.db.dto.export.ExportedDatabaseDto;
import com.university.db.entity.Database;

import java.util.stream.Collectors;

public class DatabaseMapper {

    public static DatabaseDto databaseToDatabaseDto(Database d) {
        DatabaseDto dto = new DatabaseDto();
        dto.setId(d.getId());
        dto.setName(d.getName());
        return dto;
    }

    public static ExportedDatabaseDto databaseToExportedDatabaseDto(Database d) {
        ExportedDatabaseDto dto = new ExportedDatabaseDto();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setTables(d.getTables().stream()
                .map(TableMapper::tableToExportedTableDto).collect(Collectors.toList()));
        return dto;
    }

}
