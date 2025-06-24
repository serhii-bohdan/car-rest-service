package ua.foxminded.carrestservice.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a car model in the car rest service system.
 * Extends {@link AbstractEntity} to inherit an auto-incremented ID. Stores the model's name and
 * maintains a many-to-one relationship with {@link Manufacturer} and a one-to-many relationship
 * with {@link Car} entities. Uses Lombok annotations for boilerplate code reduction and is mapped
 * to the {@code models} table.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Manufacturer
 * @see Car
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "manufacturer"})
@ToString(callSuper = true, exclude = {"manufacturer", "cars"})
@SuperBuilder
@Entity
@Table(name = "models")
public class Model extends AbstractEntity {

    /**
     * The name of the car model.
     * Stored in the {@code name} column of the {@code models} table.
     */
    @Column(name = "name")
    private String name;

    /**
     * The manufacturer associated with this car model.
     * Represents a many-to-one relationship with lazy fetching, linked via the {@code manufacturer_id} column.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    /**
     * Set of cars associated with this car model.
     * Represents a one-to-many relationship with lazy fetching and cascading removal.
     */
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private Set<Car> cars = new HashSet<>();

}
