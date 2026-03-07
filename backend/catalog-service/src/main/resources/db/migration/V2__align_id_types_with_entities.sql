-- Align DB column types with JPA entities (Long -> BIGINT)
ALTER TABLE restaurants
    ALTER COLUMN id TYPE BIGINT;

ALTER TABLE products
    ALTER COLUMN id TYPE BIGINT;
