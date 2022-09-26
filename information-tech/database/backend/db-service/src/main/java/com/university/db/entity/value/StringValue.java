package com.university.db.entity.value;

import com.university.db.entity.ValueType;

public class StringValue extends Value {

    private String data;

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }
}
