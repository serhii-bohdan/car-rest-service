package ua.foxminded.carrestservice.dto.request;

import lombok.*;

/**
 * Data Transfer Object (DTO) for specifying car search criteria in the car rest service system.
 * Represents parameters for filtering cars by manufacturer, model, category, and year range.
 * Annotated with Lombok annotations to generate getters, setters, constructors, toString, and
 * builder pattern implementation.
 *
 * @author Serhii Bohdan
 * @see lombok.Getter
 * @see lombok.Setter
 * @see lombok.AllArgsConstructor
 * @see lombok.NoArgsConstructor
 * @see lombok.ToString
 * @see lombok.Builder
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CarSearchRequestDto {

    /**
     * The name of the manufacturer to filter cars.
     */
    private String manufacturer;

    /**
     * The name of the model to filter cars.
     */
    private String model;

    /**
     * The category to filter cars.
     */
    private String category;

    /**
     * The minimum year of manufacture to filter cars.
     */
    private Integer minYear;

    /**
     * The maximum year of manufacture to filter cars.
     */
    private Integer maxYear;

}
