package ua.foxminded.carrestservice.entity;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a car in the car rest service system.
 * Extends {@link AbstractEntity} to inherit an auto-incremented ID. Stores car details such as a
 * unique object ID, production year, and maintains a many-to-one relationship with {@link Model}
 * and a many-to-many relationship with {@link Category} entities. Uses Lombok annotations for
 * boilerplate code reduction and is mapped to the {@code cars} table.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Model
 * @see Category
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"objectId"})
@ToString(callSuper = true, exclude = {"model", "categories"})
@SuperBuilder
@Entity
@Table(name = "cars")
public class Car extends AbstractEntity {

    /**
     * Unique identifier for the car.
     * Stored in the {@code object_id} column of the {@code cars} table with a unique constraint.
     */
    @Column(name = "object_id", unique = true)
    private String objectId;

    /**
     * The production year of the car.
     * Stored in the {@code production_year} column of the {@code cars} table.
     */
    @Column(name = "production_year")
    private Year productionYear;

    /**
     * The model associated with this car.
     * Represents a many-to-one relationship with lazy fetching, linked via the {@code model_id} column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    /**
     * Set of categories associated with this car.
     * Represents a many-to-many relationship with lazy fetching, mapped via the {@code cars_categories} table.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cars_categories",
        joinColumns = @JoinColumn(name = "car_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

}
