package com.university.db.dto;

import com.university.db.entity.ValueType;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ColumnDto extends RepresentationModel<ColumnDto> {

    private String id;
    private String name;
    private ValueType type;

}
