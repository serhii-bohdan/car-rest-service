CREATE TABLE manufacturers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE models (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    manufacturer_id BIGINT REFERENCES manufacturers (id)
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    object_id VARCHAR UNIQUE NOT NULL,
    production_year INTEGER NOT NULL,
    model_id BIGINT REFERENCES models (id)
);

CREATE TABLE cars_categories (
    car_id BIGINT REFERENCES cars (id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories (id) ON DELETE CASCADE,
    PRIMARY KEY (car_id, category_id)
);
