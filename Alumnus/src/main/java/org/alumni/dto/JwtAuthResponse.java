package org.alumni.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A Data Transfer Object (DTO) that represents the response sent to the client
 * after a successful authentication. It contains the JSON Web Token (JWT)
 * needed for accessing protected endpoints.
 */
@Getter
@Setter
public class JwtAuthResponse {

    /**
     * The generated JSON Web Token (JWT). The client must include this
     * token in the 'Authorization' header of subsequent requests.
     */
    private String accessToken;

    /**
     * The type of the token. By convention, for JWTs, this is 'Bearer'.
     * The 'Authorization' header should be formatted as: "Bearer <accessToken>".
     */
    private String tokenType = "Bearer";

    /**
     * Constructor to create a new JwtAuthResponse.
     * @param accessToken The JWT generated for the authenticated user.
     */
    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}