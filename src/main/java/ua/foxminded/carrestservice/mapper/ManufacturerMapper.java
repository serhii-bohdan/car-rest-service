package ua.foxminded.carrestservice.mapper;

import org.mapstruct.*;
import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.update.ManufacturerUpdateDto;
import ua.foxminded.carrestservice.entity.Manufacturer;

/**
 * MapStruct mapper interface for converting between {@link Manufacturer} entities and their DTOs in
 * the car rest service system. Extends {@link BaseMapper} to provide mappings for
 * {@link ManufacturerCreateDto}, {@link ManufacturerUpdateDto}, and {@link ManufacturerResponseDto}.
 * Annotated with {@code @Mapper} to enable Spring integration and automatic implementation.
 *
 * @author Serhii Bohdan
 * @see BaseMapper
 * @see Manufacturer
 * @see ManufacturerCreateDto
 * @see ManufacturerUpdateDto
 * @see ManufacturerResponseDto
 * @see org.mapstruct.Mapper
 */
@Mapper(componentModel = "spring")
public interface ManufacturerMapper extends BaseMapper<Manufacturer, ManufacturerCreateDto, ManufacturerUpdateDto, ManufacturerResponseDto> {
}
