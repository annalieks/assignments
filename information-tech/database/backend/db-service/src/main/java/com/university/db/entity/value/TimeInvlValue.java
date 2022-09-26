package com.university.db.entity.value;

import com.university.db.entity.ValueType;

import java.time.LocalTime;

public class TimeInvlValue extends Value {

    private LocalTime start;
    private LocalTime end;

    @Override
    public ValueType getType() {
        return ValueType.TIMEINVL;
    }
}
