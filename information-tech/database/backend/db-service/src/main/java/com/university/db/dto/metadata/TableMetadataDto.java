package com.university.db.dto.metadata;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TableMetadataDto {

    @NotBlank
    private String name;

}
