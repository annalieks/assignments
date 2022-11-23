package com.university.db.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TableDto extends RepresentationModel<TableDto> {

    private String id;
    private String name;

}
