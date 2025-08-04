package ua.foxminded.carrestservice.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
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
    @Parameter(description = "Name of the manufacturer to filter cars", example = "Audi")
    private String manufacturer;

    /**
     * The name of the model to filter cars.
     */
    @Parameter(description = "Name of the model to filter cars", example = "A4")
    private String model;

    /**
     * The category to filter cars.
     */
    @Parameter(description = "Category to filter cars", example = "Sedan")
    private String category;

    /**
     * The minimum year of manufacture to filter cars.
     */
    @Parameter(description = "Minimum year of manufacture to filter cars", example = "2010")
    private Integer minYear;

    /**
     * The maximum year of manufacture to filter cars.
     */
    @Parameter(description = "Maximum year of manufacture to filter cars", example = "2023")
    private Integer maxYear;

}
