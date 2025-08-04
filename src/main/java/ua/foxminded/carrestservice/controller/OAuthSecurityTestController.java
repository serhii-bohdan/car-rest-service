package ua.foxminded.carrestservice.controller;

import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.BAD_REQUEST_RESPONSE;
import static ua.foxminded.carrestservice.util.openapi.OAuthSchemaExample.OAUTH_TOKEN_RESPONSE;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;
import ua.foxminded.carrestservice.dto.response.OAuthAccessTokenDto;
import ua.foxminded.carrestservice.service.OAuthSecurityTestService;

/**
 * REST controller for testing OAuth security operations in the car rest service system.
 * Handles HTTP requests related to retrieving OAuth access tokens for testing purposes.
 * Annotated with {@code @RestController} to mark it as a Spring REST controller,
 * {@code @RequiredArgsConstructor} to generate a constructor for final fields,
 * and {@code @RequestMapping} to define the base endpoint {@code /api/v1/security-test}.
 *
 * @author Serhii Bohdan
 * @see org.springframework.web.bind.annotation.RestController
 * @see lombok.RequiredArgsConstructor
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @see ua.foxminded.carrestservice.service.OAuthSecurityTestService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/security-test")
@Tag(name = "OAuth Security Test", description = "API for testing OAuth token retrieval")
public class OAuthSecurityTestController {

    /**
     * Service for handling OAuth security test operations, used to retrieve access tokens.
     */
    private final OAuthSecurityTestService securityTestService;

    /**
     * Retrieves an OAuth access token for testing purposes.
     * Sends a request to the OAuth service via {@link OAuthSecurityTestService} and returns the token details.
     * No authentication is required for this endpoint.
     *
     * @return a {@link ResponseEntity} containing an {@link OAuthAccessTokenDto} with the access token,
     * expiration time, and token type
     */
    @Operation(summary = "Get OAuth access token for testing",
        description = "Retrieves an OAuth access token for testing purposes. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = OAuthAccessTokenDto.class, example = OAUTH_TOKEN_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input or request",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @GetMapping("/token")
    public ResponseEntity<OAuthAccessTokenDto> getAuthorizationToken() {
        return ResponseEntity.ok(securityTestService.getAccessTokenForTest());
    }

}
