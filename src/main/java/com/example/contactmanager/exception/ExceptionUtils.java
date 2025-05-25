package com.example.contactmanager.exception;

public class ExceptionUtils {

    public static ResourceNotFoundException createContactNotFoundException(Long id) {
        return new ResourceNotFoundException(String.format("Contact with id %d not found", id));
    }
}
