package ua.foxminded.carrestservice.util.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.entity.Manufacturer;
import ua.foxminded.carrestservice.util.mapping.annotation.ModelManufacturerUpdateMapping;

/**
 * Utility class for custom mappings related to {@link Manufacturer} in the car rest service system.
 * Provides a method for converting a manufacturer ID to a {@link Manufacturer} entity, used by
 * MapStruct mappers. Annotated with {@code @Component} for Spring integration and {@code @Slf4j}
 * for logging.
 *
 * @author Serhii Bohdan
 * @see ua.foxminded.carrestservice.mapper.ModelMapper
 * @see ModelManufacturerUpdateMapping
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Slf4j
@Component
public class ModelUtilMapper {

    /**
     * Converts a manufacturer ID to a {@link Manufacturer} entity.
     * Creates a {@link Manufacturer} with the specified ID. Annotated with
     * {@link ModelManufacturerUpdateMapping} for MapStruct integration.
     *
     * @param manufacturerId the ID of the manufacturer
     * @return the {@link Manufacturer} entity with the specified ID
     */
    @ModelManufacturerUpdateMapping
    public Manufacturer convertToManufacturer(Long manufacturerId) {
        log.info("Converting manufacturerId to Manufacturer: {}", manufacturerId);
        Manufacturer manufacturer = Manufacturer.builder()
            .id(manufacturerId)
            .build();

        log.debug("Converted to Manufacturer: {}", manufacturer);
        return manufacturer;
    }

}
