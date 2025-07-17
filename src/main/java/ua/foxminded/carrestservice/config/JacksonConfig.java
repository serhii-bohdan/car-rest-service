package ua.foxminded.carrestservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Configuration class for customizing Jackson serialization in the car rest service system.
 * Annotated with {@code @Configuration} to mark it as a Spring configuration class. Enables
 * Spring Data web support with {@code @EnableSpringDataWebSupport} and configures page
 * serialization to use DTOs via {@code PageSerializationMode.VIA_DTO}.
 *
 * @author Serhii Bohdan
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.data.web.config.EnableSpringDataWebSupport
 * @see org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class JacksonConfig {
}
