package com.university.db.dto.metadata;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RowMetadataDto extends RepresentationModel<RowMetadataDto> {

    @NotEmpty
    private List<String> fields;

}
