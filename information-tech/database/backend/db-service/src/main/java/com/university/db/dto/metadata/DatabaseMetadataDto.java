package com.university.db.dto.metadata;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DatabaseMetadataDto {

    @NotBlank
    private String name;

}