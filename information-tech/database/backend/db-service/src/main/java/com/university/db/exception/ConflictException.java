package com.university.db.exception;

public class ConflictException extends EntityException {

    public ConflictException(String message) {
        super(message);
    }

}
