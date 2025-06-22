package org.alumni.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) used to handle requests for password resets.
 * It carries the email address of the user who has forgotten their password.
 */
@Data // A Lombok annotation that generates getters, setters, toString(), equals(), and hashCode()
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class ForgotPasswordRequest {

    /**
     * The user's email address.
     * This field is validated to ensure it's not blank and is in a proper email format.
     */
    @NotBlank(message = "Email address cannot be blank.")
    @Email(message = "Please provide a valid email address format.")
    private String email;

}