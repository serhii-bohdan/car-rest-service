package ua.foxminded.carrestservice.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.foxminded.carrestservice.dto.create.AbstractCreateDto;
import ua.foxminded.carrestservice.dto.response.AbstractResponseDto;
import ua.foxminded.carrestservice.dto.update.AbstractUpdateDto;
import ua.foxminded.carrestservice.entity.AbstractEntity;

/**
 * Generic MapStruct mapper interface for converting between entities and DTOs in the car rest service system.
 * Defines mappings between {@link AbstractEntity} and its corresponding DTOs: {@link AbstractCreateDto} for creation,
 * {@link AbstractUpdateDto} for updates, and {@link AbstractResponseDto} for responses. Supports single and collection-based
 * mappings using MapStruct annotations.
 *
 * @param <E> the entity type extending {@link AbstractEntity}
 * @param <C> the creation DTO type extending {@link AbstractCreateDto}
 * @param <U> the update DTO type extending {@link AbstractUpdateDto}
 * @param <R> the response DTO type extending {@link AbstractResponseDto}
 * @author Serhii Bohdan
 */
public interface BaseMapper<E extends AbstractEntity, C extends AbstractCreateDto,
    U extends AbstractUpdateDto, R extends AbstractResponseDto> {

    /**
     * Maps a creation DTO to a new entity.
     *
     * @param createDto the creation DTO to map
     * @return the mapped entity
     */
    E toCreateEntity(C createDto);

    /**
     * Maps an update DTO to an existing entity, ignoring null properties.
     *
     * @param updateDto    the update DTO to map
     * @param targetEntity the target entity to update
     * @return the updated entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E toUpdateEntity(U updateDto, @MappingTarget E targetEntity);

    /**
     * Maps an entity to a response DTO.
     *
     * @param entity the entity to map
     * @return the mapped response DTO
     */
    R toResponseDto(E entity);

    /**
     * Maps a collection of entities to a list of response DTOs.
     *
     * @param entities the collection of entities to map
     * @return a list of mapped response DTOs
     */
    List<R> toResponseDtoList(Collection<E> entities);

}
