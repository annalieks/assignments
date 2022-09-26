package com.university.db.entity.value;

import com.university.db.entity.ValueType;
import com.university.db.entity.value.Value;

import java.time.LocalTime;

public class TimeValue extends Value {

    private LocalTime data;

    @Override
    public ValueType getType() {
        return ValueType.TIME;
    }
}
