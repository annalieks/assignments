package com.university.db.entity.value;

import com.university.db.entity.ValueType;

public class IntegerValue extends Value {

    private Integer data;

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }
}
