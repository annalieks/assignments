package com.university.db.entity.value;

import com.university.db.entity.ValueType;

public class CharValue extends Value {

    private Character data;

    @Override
    public ValueType getType() {
        return ValueType.CHAR;
    }
}
