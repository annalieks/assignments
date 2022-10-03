package com.university.db.mapper;

import com.university.db.dto.RowDto;
import com.university.db.entity.Row;

import java.util.stream.Collectors;

public class RowMapper {

    public static RowDto rowToRowDto(Row r) {
        RowDto dto = new RowDto();
        dto.setId(r.getId());
        dto.setFields(r.getFields());
        return dto;
    }

}
