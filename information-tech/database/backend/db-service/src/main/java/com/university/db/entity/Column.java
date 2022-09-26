package com.university.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Column {

    @Id
    private String id;
    private String name;
    private ValueType type;

}
