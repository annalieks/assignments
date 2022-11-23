package com.university.db.dto.metadata;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

@Data
public class TableMetadataDto extends RepresentationModel<TableMetadataDto> {

    @NotBlank
    private String name;

}
