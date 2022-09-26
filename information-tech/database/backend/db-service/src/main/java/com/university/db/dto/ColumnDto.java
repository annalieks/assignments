package com.university.db.dto;

import com.university.db.entity.ValueType;
import lombok.Data;

@Data
public class ColumnDto {

    private String id;
    private String name;
    private ValueType type;

}
