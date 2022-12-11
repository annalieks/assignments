package com.example.equationcalculator.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + StudentEntry.TABLE_NAME + " (" +
                    StudentEntry._ID + " INTEGER PRIMARY KEY," +
                    StudentEntry.COLUMN_FULL_NAME + " TEXT," +
                    StudentEntry.COLUMN_MARK1 + " INTEGER," +
                    StudentEntry.COLUMN_MARK2 + " INTEGER);";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StudentEntry.TABLE_NAME;

    private DatabaseContract() {
    }

    /* Inner class that defines the table contents */
    public static class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_MARK1 = "mark1";
        public static final String COLUMN_MARK2 = "mark2";
    }

}
