package ru.surpavel.bugtrackingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceAlreadyExistsException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException() {
        super();
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
