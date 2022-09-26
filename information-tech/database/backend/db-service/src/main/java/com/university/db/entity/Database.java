package com.university.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
public class Database {

    @Id
    private String id;
    @Indexed
    private String name;
    private List<Table> tables = new ArrayList<>();

}
