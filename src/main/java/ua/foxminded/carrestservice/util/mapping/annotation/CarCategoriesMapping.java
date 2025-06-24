package ua.foxminded.carrestservice.util.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Qualifier;

/**
 * MapStruct qualifier annotation for custom mapping of car categories in the car rest service system.
 * Used to mark methods that handle the mapping of category-related fields, such as {@code categoryIds}
 * to {@code categories} in {@link ua.foxminded.carrestservice.entity.Car} entities or DTOs. Annotated
 * with {@code @Qualifier} for MapStruct integration, {@code @Target} to restrict usage to methods,
 * and {@code @Retention} to retain the annotation at compilation.
 *
 * @author Serhii Bohdan
 * @see org.mapstruct.Qualifier
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 */
@Qualifier
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface CarCategoriesMapping {
}
