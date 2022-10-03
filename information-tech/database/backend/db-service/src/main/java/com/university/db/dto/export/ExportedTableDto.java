package com.university.db.dto.export;

import com.university.db.dto.ColumnDto;
import com.university.db.dto.RowDto;
import lombok.Data;

import java.util.List;

@Data
public class ExportedTableDto {

    private String id;
    private String name;
    private List<ColumnDto> columns;
    private List<RowDto> rows;

}
