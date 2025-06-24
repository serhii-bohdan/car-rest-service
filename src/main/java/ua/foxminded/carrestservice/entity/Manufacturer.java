package ua.foxminded.carrestservice.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a car manufacturer in the car rest service system.
 * Extends {@link AbstractEntity} to inherit an auto-incremented ID. Stores the manufacturer's name
 * and maintains a one-to-many relationship with {@link Model} entities. Uses Lombok annotations
 * for boilerplate code reduction and is mapped to the "manufacturers" table.
 *
 * @author Serhii Bohdan
 * @see AbstractEntity
 * @see Model
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
@ToString(callSuper = true, exclude = {"models"})
@SuperBuilder
@Entity
@Table(name = "manufacturers")
public class Manufacturer extends AbstractEntity {

    /**
     * The name of the manufacturer.
     * Unique field stored in the "name" column of the "manufacturers" table.
     */
    @Column(name = "name", unique = true)
    private String name;

    /**
     * Set of car models associated with this manufacturer.
     * Represents a one-to-many relationship with lazy fetching and cascading removal.
     */
    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private Set<Model> models = new HashSet<>();

}
