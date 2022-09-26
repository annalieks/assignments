package com.university.db.dto;

import com.university.db.entity.value.Value;
import lombok.Data;

@Data
public class FieldDto {

    private String id;
    private String columnId;
    private Value value;

}
