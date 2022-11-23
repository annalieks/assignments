package com.university.db.dto.metadata;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

@Data
public class ColumnEditableMetadataDto extends RepresentationModel<ColumnEditableMetadataDto> {

    @NotBlank
    private String name;

}
