package com.university.db.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.DateTimeException;
import java.time.LocalTime;

public enum ValueType {
    INTEGER,
    REAL,
    CHAR,
    STRING,
    TIME,
    TIMEINVL;

    public static boolean validate(ValueType type, String value) {
        if (value == null || type == STRING) {
            return true;
        }
        if (type == INTEGER) {
            return StringUtils.isNumeric(value);
        }
        if (type == REAL) {
            return NumberUtils.isCreatable(value);
        }
        if (type == CHAR) {
            return value.length() == 1;
        }
        if (type == TIME) {
            return validateTimeFormat(value);
        }
        if (type == TIMEINVL) {
            return validateTimeInterval(value);
        }
        return false;
    }

    private static boolean validateTimeFormat(String value) {
        try {
            LocalTime.parse(value);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    private static boolean validateTimeInterval(String value) {
        String[] parts = value.split(";");
        if (parts.length != 2) {
            return false;
        }
        boolean isValid = validateTimeFormat(parts[0]) && validateTimeFormat(parts[1]);
        if (!isValid) {
            return false;
        }
        LocalTime start = LocalTime.parse(parts[0]);
        LocalTime end = LocalTime.parse(parts[1]);
        return !start.isAfter(end);
    }
}
