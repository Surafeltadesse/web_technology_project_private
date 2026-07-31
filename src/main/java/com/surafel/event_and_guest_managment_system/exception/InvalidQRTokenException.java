package com.surafel.event_and_guest_managment_system.exception;

public class InvalidQRTokenException extends RuntimeException {
    public InvalidQRTokenException(String message) {
        super(message);
    }
}
