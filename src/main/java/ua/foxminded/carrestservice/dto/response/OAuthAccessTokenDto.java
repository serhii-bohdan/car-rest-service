package ua.foxminded.carrestservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Data Transfer Object (DTO) for representing an OAuth access token response.
 * This class encapsulates the details of an OAuth access token, including the token value,
 * expiration time, and token type, as returned by an OAuth authorization server.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OAuthAccessTokenDto {

    /**
     * The OAuth access token used for authenticating API requests.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * The duration in seconds until the access token expires.
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * The type of the token, typically "Bearer".
     */
    @JsonProperty("token_type")
    private String tokenType;

}
