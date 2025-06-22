package org.alumni.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLoginRequest {
    @NotBlank
    private String provider; // e.g., "google", "facebook"

    @NotBlank
    private String token; // The ID Token from the social provider
}