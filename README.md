# Car REST Service

### Entity Relationship Diagram

The entity relationship diagram below illustrates the data model used in the service, showing the relationships
between `Car`, `Model`, `Manufacturer`, and `Category` entities. <br>
![entity relationship diagram](docs/car-rest-service-entity-diagram.svg) <br>
Entities and Attributes
1. **AbstractEntity** (Abstract Class)
    - **Description**: A base class providing a common identifier for all entities.
    - **Attributes**:
        - `id: Long` — Unique identifier (primary key, auto-incremented).


2. **Car** (Extends AbstractEntity)
    - **Description**: Represents an individual car in the system.
    - **Attributes**:
        - `objectId: String` — Natural identifier (unique, corresponds to `objectId` in the CSV).
        - `productionYear: Year` — Year of manufacture (e.g., 2020).
        - `model: Model` — Reference to the car’s model.
        - `categories: Set<Category>` — Collection of categories the car belongs to (e.g., SUV, Sedan).


3. **Model** (Extends AbstractEntity)
    - **Description**: Represents a specific car model produced by a manufacturer (e.g., Audi Q3).
    - **Attributes**:
        - `name: String` — Name of the model (e.g., "Q3").
        - `manufacturer: Manufacturer` — Reference to the manufacturer producing this model.


4. **Manufacturer** (Extends AbstractEntity)
    - **Description**: Represents a car manufacturer or brand (e.g., Audi, Chevrolet).
    - **Attributes**:
        - `name: String` — Name of the manufacturer (e.g., "Audi").


5. **Category** (Extends AbstractEntity)
    - **Description**: Represents a category or type of car (e.g., SUV, Sedan, Coupe).
    - **Attributes**:
        - `name: String` — Name of the category (e.g., "SUV").

### Relationships

- **Car to Model**: Many-to-one (each car has one model; a model can have many cars).
- **Model to Manufacturer**: Many-to-one (each model has one manufacturer; a manufacturer can have many models).
- **Car to Category**: Many-to-many (a car can belong to multiple categories; a category can apply to multiple
  cars). <br>

## Task 4.1 Planning: Car Database

**Important:** In the next series of tasks you're going to develop Car Database microservice with rest API, make sure to
give repo a meaningful name (ex. **car-rest-service**)

**Assignment** <br>
Analyze and decompose Car DB Reset service  (create UML class diagram for application) based on attached csv data. <br>

- Decompose provided data into db entities

[📄 file.csv](docs/file.csv)