package ua.foxminded.carrestservice.mapper;

import org.mapstruct.*;
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.entity.Car;
import ua.foxminded.carrestservice.util.mapping.annotation.CarCategoriesMapping;
import ua.foxminded.carrestservice.util.mapping.annotation.CarModelUpdateMapping;
import ua.foxminded.carrestservice.util.mapping.annotation.CarObjectIdMapping;
import ua.foxminded.carrestservice.util.mapping.annotation.CarProductionYearMapping;
import ua.foxminded.carrestservice.util.mapping.CarUtilMapper;

/**
 * MapStruct mapper interface for converting between {@link Car} entities and their DTOs in the car
 * rest service system. Extends {@link BaseMapper} to provide mappings for {@link CarCreateDto},
 * {@link CarUpdateDto}, and {@link CarResponseDto}. Uses {@link ModelMapper}, {@link CategoryMapper},
 * and {@link CarUtilMapper} for nested mappings. Annotated with {@code @Mapper} for Spring integration.
 *
 * @author Serhii Bohdan
 * @see BaseMapper
 * @see Car
 * @see CarCreateDto
 * @see CarUpdateDto
 * @see CarResponseDto
 * @see ModelMapper
 * @see CategoryMapper
 * @see CarUtilMapper
 * @see org.mapstruct.Mapper
 */
@Mapper(componentModel = "spring", uses = {ModelMapper.class, CategoryMapper.class, CarUtilMapper.class})
public interface CarMapper extends BaseMapper<Car, CarCreateDto, CarUpdateDto, CarResponseDto> {

    /**
     * Maps a creation DTO to a new {@link Car} entity.
     * Overrides {@link BaseMapper#toCreateEntity} with custom mappings: {@code objectId} via
     * {@link CarObjectIdMapping}, {@code productionYear} via {@link CarProductionYearMapping},
     * {@code modelId} to {@code model.id}, and {@code categoryIds} to {@code categories} via
     * {@link CarCategoriesMapping}.
     *
     * @param dto the creation DTO to map
     * @return the mapped {@link Car} entity
     */
    @Override
    @Mapping(source = "objectId", target = "objectId", qualifiedBy = {CarObjectIdMapping.class})
    @Mapping(source = "productionYear", target = "productionYear", qualifiedBy = {CarProductionYearMapping.class})
    @Mapping(source = "modelId", target = "model.id")
    @Mapping(source = "categoryIds", target = "categories", qualifiedBy = {CarCategoriesMapping.class})
    Car toCreateEntity(CarCreateDto dto);

    /**
     * Maps an update DTO to an existing {@link Car} entity, ignoring null properties.
     * Overrides {@link BaseMapper#toUpdateEntity} with custom mappings: {@code productionYear} via
     * {@link CarProductionYearMapping}, {@code modelId} to {@code model} via
     * {@link CarModelUpdateMapping}, and {@code categoryIds} to {@code categories} via
     * {@link CarCategoriesMapping}. Uses {@code NullValuePropertyMappingStrategy.IGNORE}.
     *
     * @param updateDto    the update DTO to map
     * @param targetEntity the target {@link Car} entity to update
     * @return the updated {@link Car} entity
     */
    @Override
    @Mapping(source = "productionYear", target = "productionYear", qualifiedBy = {CarProductionYearMapping.class})
    @Mapping(source = "modelId", target = "model", qualifiedBy = {CarModelUpdateMapping.class})
    @Mapping(source = "categoryIds", target = "categories", qualifiedBy = {CarCategoriesMapping.class})
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Car toUpdateEntity(CarUpdateDto updateDto, @MappingTarget Car targetEntity);

    /**
     * Maps a {@link Car} entity to a response DTO.
     * Overrides {@link BaseMapper#toResponseDto} to map {@code productionYear.value} from the
     * entity to {@code productionYear} in the {@link CarResponseDto}.
     *
     * @param entity the {@link Car} entity to map
     * @return the mapped {@link CarResponseDto}
     */
    @Override
    @Mapping(source = "productionYear.value", target = "productionYear")
    CarResponseDto toResponseDto(Car entity);

}
