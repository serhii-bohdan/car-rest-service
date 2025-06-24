package ua.foxminded.carrestservice.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a car category in the car rest service system.
 * Extends {@link AbstractEntity} to inherit an auto-incremented ID. Stores the category's name and
 * maintains a many-to-many relationship with {@link Car} entities. Uses Lombok annotations for
 * boilerplate code reduction and is mapped to the "categories" table.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Car
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
@ToString(callSuper = true, exclude = {"cars"})
@SuperBuilder
@Entity
@Table(name = "categories")
public class Category extends AbstractEntity {

    /**
     * The name of the category.
     * Unique field stored in the "name" column of the "categories" table.
     */
    @Column(name = "name", unique = true)
    private String name;

    /**
     * Set of cars associated with this category.
     * Represents a many-to-many relationship with lazy fetching and cascading removal.
     */
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private Set<Car> cars = new HashSet<>();

}
