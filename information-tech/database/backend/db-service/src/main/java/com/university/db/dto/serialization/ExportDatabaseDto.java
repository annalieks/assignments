package com.university.db.dto.serialization;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExportDatabaseDto extends RepresentationModel<ExportDatabaseDto> {

    @NotBlank
    private String name;
    private List<ExportTableDto> tables = new ArrayList<>();

}
