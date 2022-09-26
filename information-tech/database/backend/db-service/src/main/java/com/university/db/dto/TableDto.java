package com.university.db.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableDto {

    private String id;
    private String name;
    private List<ColumnDto> columns;
    private List<RowDto> rows;

}
