package org.alumni.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role must be 'Alumni', 'Student', or 'Admin'")
    private String role; // "Alumni", "Student", or "Admin"

    // Optional fields
    private String companyName;
    private String companyRole;
    private String collegeName;
    private String branch;
    private String collegeId;
}