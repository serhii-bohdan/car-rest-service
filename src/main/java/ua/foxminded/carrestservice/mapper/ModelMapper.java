package ua.foxminded.carrestservice.mapper;

import org.mapstruct.*;
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.entity.Model;
import ua.foxminded.carrestservice.util.mapping.ModelUtilMapper;
import ua.foxminded.carrestservice.util.mapping.annotation.ModelManufacturerUpdateMapping;

/**
 * MapStruct mapper interface for converting between {@link Model} entities and their DTOs in the car
 * rest service system. Extends {@link BaseMapper} to provide mappings for {@link ModelCreateDto},
 * {@link ModelUpdateDto}, and {@link ModelResponseDto}. Uses {@link ManufacturerMapper} and
 * {@link ModelUtilMapper} for nested mappings. Annotated with {@code @Mapper} for Spring integration.
 *
 * @author Serhii Bohdan
 * @see BaseMapper
 * @see Model
 * @see ModelCreateDto
 * @see ModelUpdateDto
 * @see ModelResponseDto
 * @see ManufacturerMapper
 * @see ModelUtilMapper
 * @see org.mapstruct.Mapper
 */
@Mapper(componentModel = "spring", uses = {ManufacturerMapper.class, ModelUtilMapper.class})
public interface ModelMapper extends BaseMapper<Model, ModelCreateDto, ModelUpdateDto, ModelResponseDto> {

    /**
     * Maps a creation DTO to a new {@link Model} entity.
     * Overrides {@link BaseMapper#toCreateEntity} to map {@code manufacturerId} from
     * {@link ModelCreateDto} to the {@code manufacturer.id} field of the entity.
     *
     * @param createDto the creation DTO to map
     * @return the mapped {@link Model} entity
     */
    @Override
    @Mapping(source = "manufacturerId", target = "manufacturer.id")
    Model toCreateEntity(ModelCreateDto createDto);

    /**
     * Maps an update DTO to an existing {@link Model} entity, ignoring null properties.
     * Overrides {@link BaseMapper#toUpdateEntity} to map {@code manufacturerId} from
     * {@link ModelUpdateDto} to the {@code manufacturer} field using a custom mapping defined by
     * {@link ModelManufacturerUpdateMapping}. Uses {@code NullValuePropertyMappingStrategy.IGNORE}.
     *
     * @param updateDto    the update DTO to map
     * @param targetEntity the target {@link Model} entity to update
     * @return the updated {@link Model} entity
     */
    @Override
    @Mapping(source = "manufacturerId", target = "manufacturer", qualifiedBy = {ModelManufacturerUpdateMapping.class})
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Model toUpdateEntity(ModelUpdateDto updateDto, @MappingTarget Model targetEntity);

}
