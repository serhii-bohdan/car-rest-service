package ua.foxminded.carrestservice.service;

import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.update.ManufacturerUpdateDto;
import ua.foxminded.carrestservice.entity.Manufacturer;

/**
 * Service interface for managing {@link Manufacturer} entities in
 * the car rest service system. Extends {@link BaseService} to provide CRUD operations for
 * {@link ManufacturerCreateDto}, {@link ManufacturerUpdateDto}, and {@link ManufacturerResponseDto}.
 *
 * @author Serhii Bohdan
 * @see BaseService
 * @see ManufacturerCreateDto
 * @see ManufacturerUpdateDto
 * @see ManufacturerResponseDto
 */
public interface ManufacturerService extends BaseService<ManufacturerCreateDto, ManufacturerUpdateDto, ManufacturerResponseDto> {
}
