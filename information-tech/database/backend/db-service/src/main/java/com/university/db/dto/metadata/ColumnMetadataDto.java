package com.university.db.dto.metadata;

import com.university.db.entity.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnMetadataDto extends RepresentationModel<ColumnMetadataDto> {

    @NotBlank
    private String name;
    @NonNull
    private ValueType type;

}
