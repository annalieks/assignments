package com.university.db.dto.metadata;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RowMetadataDto {

    @NotEmpty
    private List<String> fields;

}
