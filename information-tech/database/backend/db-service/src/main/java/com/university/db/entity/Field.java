package com.university.db.entity;

import com.university.db.entity.value.Value;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Field {

    @Id
    private String id;
    private String columnId;
    private Value value;

}
