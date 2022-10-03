package com.university.db.mapper;

import com.university.db.dto.ColumnDto;
import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.entity.Column;

public class ColumnMapper {

    public static ColumnDto columnToColumnDto(Column c) {
        ColumnDto dto = new ColumnDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setType(c.getType());
        return dto;
    }

    public static Column columnMetadataDtoToColumn(ColumnMetadataDto dto) {
        Column column = new Column();
        column.setName(dto.getName());
        column.setType(dto.getType());
        return column;
    }

}
