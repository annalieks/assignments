package com.university.db.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.annotation.Id;

import java.util.Comparator;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Column {

    @Id
    private String id;
    private String name;
    private ValueType type;

}
