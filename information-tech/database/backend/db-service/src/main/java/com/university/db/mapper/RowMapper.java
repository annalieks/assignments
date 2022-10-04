package com.university.db.mapper;

import com.university.db.dto.RowDto;
import com.university.db.dto.metadata.RowMetadataDto;
import com.university.db.entity.Row;

import java.util.stream.Collectors;

public class RowMapper {

    public static RowDto rowToRowDto(Row r) {
        RowDto dto = new RowDto();
        dto.setId(r.getId());
        dto.setFields(r.getFields());
        return dto;
    }

    public static RowMetadataDto rowToRowMetadataDto(Row r) {
        RowMetadataDto dto = new RowMetadataDto();
        dto.setFields(r.getFields());
        return dto;
    }

    public static Row rowMetadataDtoToRow(RowMetadataDto dto) {
        Row row = new Row();
        row.setFields(dto.getFields());
        return row;
    }

}
