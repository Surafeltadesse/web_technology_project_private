package com.surafel.event_and_guest_managment_system.exception;

public class AlreadyCheckedInException extends RuntimeException {
    public AlreadyCheckedInException(String message) {
        super(message);
    }
}
