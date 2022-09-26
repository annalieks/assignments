package com.university.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {

    @Id
    private String id;
    private String name;
    private List<Column> columns = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

}
