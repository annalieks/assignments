package com.university.db.dto.metadata;

import com.university.db.entity.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ColumnMetadataDto {

    private String name;
    @NonNull
    private ValueType type;

}
