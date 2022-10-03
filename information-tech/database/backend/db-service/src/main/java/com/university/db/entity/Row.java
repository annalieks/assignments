package com.university.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class Row {

    @Id
    private String id;
    private List<String> fields = new ArrayList<>();

}
