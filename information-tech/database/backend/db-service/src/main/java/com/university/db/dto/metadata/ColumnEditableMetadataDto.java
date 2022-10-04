package com.university.db.dto.metadata;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ColumnEditableMetadataDto {

    @NotBlank
    private String name;

}
