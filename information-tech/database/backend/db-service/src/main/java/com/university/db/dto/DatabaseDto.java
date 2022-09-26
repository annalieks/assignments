package com.university.db.dto;

import lombok.Data;

import java.util.List;

@Data
public class DatabaseDto {

    private String id;
    private String name;
    private List<TableDto> tables;

}
