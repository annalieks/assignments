package com.university.db.dto;

import lombok.Data;

import java.util.List;

@Data
public class RowDto {

    private String id;
    private List<FieldDto> fields;

}
