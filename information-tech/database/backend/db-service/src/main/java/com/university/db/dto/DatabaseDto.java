package com.university.db.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class DatabaseDto extends RepresentationModel<DatabaseDto> {

    private String id;
    private String name;

}
