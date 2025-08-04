package ua.foxminded.carrestservice.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.carrestservice.dto.response.OAuthAccessTokenDto;
import ua.foxminded.carrestservice.exception.AccessTokenObtainedException;
import ua.foxminded.carrestservice.service.OAuthSecurityTestService;
import ua.foxminded.carrestservice.util.security.OAuth2ClientProperties;

/**
 * Service implementation for testing OAuth security operations in the car rest service system.
 * Implements {@link OAuthSecurityTestService} to provide functionality for retrieving OAuth access tokens.
 * Uses {@link OAuth2ClientProperties} for configuration and {@link ObjectMapper} for JSON deserialization.
 * Annotated with {@code @Service} to mark it as a Spring service bean, {@code @Validated} to enable validation,
 * {@code @RequiredArgsConstructor} to generate a constructor for final fields, and {@code @Slf4j} for logging.
 *
 * @author Serhii Bohdan
 * @see org.springframework.validation.annotation.Validated
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 */
@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class OAuthSecurityTestServiceImpl implements OAuthSecurityTestService {

    /**
     * Configuration properties for OAuth2 client, including token access URL, client ID, client secret,
     * audience, and grant type.
     */
    private final OAuth2ClientProperties clientProperties;

    /**
     * Jackson ObjectMapper for deserializing JSON responses into Java objects.
     */
    private final ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     *
     * @throws AccessTokenObtainedException if an error occurs during token retrieval
     */
    @Override
    public OAuthAccessTokenDto getAccessTokenForTest() {
        log.info("Requesting OAuth token from {}", clientProperties.getTokenAccessUrl());

        HttpRequest tokenAccessRequest = HttpRequest.newBuilder()
            .uri(URI.create(clientProperties.getTokenAccessUrl()))
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody()))
            .build();

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpResponse<String> response = httpClient.send(tokenAccessRequest, HttpResponse.BodyHandlers.ofString());
            OAuthAccessTokenDto token = objectMapper.readValue(response.body(), OAuthAccessTokenDto.class);
            log.info("Successfully obtained OAuth token");
            return token;
        } catch (Exception e) {
            log.error("Failed to obtain OAuth token: {}", e.getMessage());
            throw new AccessTokenObtainedException(e.getMessage());
        }
    }

    private String buildRequestBody() {
        return "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"%s\"}"
            .formatted(clientProperties.getClientId(),
                clientProperties.getClientSecret(),
                clientProperties.getAudience(),
                clientProperties.getGrantType());
    }

}
