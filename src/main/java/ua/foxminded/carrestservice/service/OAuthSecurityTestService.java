package ua.foxminded.carrestservice.service;

import ua.foxminded.carrestservice.dto.response.OAuthAccessTokenDto;

/**
 * Service interface for testing OAuth security operations in the car rest service system.
 * Provides methods for retrieving OAuth access tokens for testing purposes.
 *
 * @author Serhii Bohdan
 */
public interface OAuthSecurityTestService {

    /**
     * Retrieves an OAuth access token for testing purposes.
     *
     * @return an {@link OAuthAccessTokenDto} containing the access token, expiration time, and token type
     */
    OAuthAccessTokenDto getAccessTokenForTest();

}
