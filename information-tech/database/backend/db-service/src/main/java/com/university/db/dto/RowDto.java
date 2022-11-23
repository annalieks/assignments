package com.university.db.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class RowDto extends RepresentationModel<RowDto> {

    private String id;
    private List<String> fields;

}
