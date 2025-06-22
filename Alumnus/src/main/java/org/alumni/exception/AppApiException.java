package org.alumni.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling application-specific API errors.
 * This allows us to throw exceptions with a specific HTTP status and a clear message.
 */
@Getter
public class AppApiException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public AppApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}