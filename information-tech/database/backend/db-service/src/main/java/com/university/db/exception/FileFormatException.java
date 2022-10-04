package com.university.db.exception;

public class FileFormatException extends Exception {

    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(String message, Exception e) {
        super(message, e);
    }

}
