package com.university.db.mapper;

import com.university.db.dto.FieldDto;
import com.university.db.entity.Field;

public class FieldMapper {

    public static FieldDto fieldToFieldDto(Field f) {
        FieldDto dto = new FieldDto();
        dto.setId(f.getId());
        dto.setColumnId(f.getColumnId());
        dto.setValue(f.getValue());
        return dto;
    }

}
