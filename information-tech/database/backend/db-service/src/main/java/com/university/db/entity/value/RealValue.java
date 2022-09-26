package com.university.db.entity.value;

import com.university.db.entity.ValueType;

public class RealValue extends Value {

    private Double data;

    @Override
    public ValueType getType() {
        return ValueType.REAL;
    }
}
