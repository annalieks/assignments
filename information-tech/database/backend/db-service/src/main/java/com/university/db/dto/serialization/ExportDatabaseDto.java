package com.university.db.dto.serialization;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExportDatabaseDto {

    @NotBlank
    private String name;
    private List<ExportTableDto> tables = new ArrayList<>();

}
