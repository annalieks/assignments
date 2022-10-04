package com.university.db.mapper;

import com.university.db.dto.TableDto;
import com.university.db.dto.metadata.TableMetadataDto;
import com.university.db.dto.serialization.ExportTableDto;
import com.university.db.entity.Table;

import java.util.stream.Collectors;

public class TableMapper {

    public static TableDto tableToTableDto(Table t) {
        TableDto dto = new TableDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        return dto;
    }

    public static ExportTableDto tableToExportedTableDto(Table t) {
        ExportTableDto dto = new ExportTableDto();
        dto.setName(t.getName());
        dto.setColumns(t.getColumns().stream()
                .map(ColumnMapper::columnToColumnMetadataDto).collect(Collectors.toList()));
        dto.setRows(t.getRows().stream()
                .map(RowMapper::rowToRowMetadataDto).collect(Collectors.toList()));
        return dto;
    }

    public static TableMetadataDto exportedTableDtoToTableMetadataDto(ExportTableDto dto) {
        TableMetadataDto metadataDto = new TableMetadataDto();
        metadataDto.setName(dto.getName());
        return metadataDto;
    }

}
