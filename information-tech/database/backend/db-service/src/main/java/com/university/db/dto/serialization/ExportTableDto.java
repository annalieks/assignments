package com.university.db.dto.serialization;

import com.university.db.dto.metadata.ColumnMetadataDto;
import com.university.db.dto.metadata.RowMetadataDto;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ExportTableDto extends RepresentationModel<ExportTableDto> {

    @NotBlank
    private String name;
    private List<ColumnMetadataDto> columns;
    private List<RowMetadataDto> rows;

}
