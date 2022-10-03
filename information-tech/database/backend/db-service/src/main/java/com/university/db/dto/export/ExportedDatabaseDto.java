package com.university.db.dto.export;

import lombok.Data;

import java.util.List;

@Data
public class ExportedDatabaseDto {

    private String id;
    private String name;
    private List<ExportedTableDto> tables;

}
