package org.alumni.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * A DTO for providing structured error details in API responses.
 * Used by the GlobalExceptionHandler.
 */
@Getter
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}