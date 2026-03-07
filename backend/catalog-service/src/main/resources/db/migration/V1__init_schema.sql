-- V1__init_schema.sql
-- Creación de la tabla restaurants
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    address VARCHAR(200),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Creación de la tabla products
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    restaurant_id BIGINT NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

-- (Opcional) Insertar un restaurante de prueba para que tengas data desde el inicio
INSERT INTO restaurants (name, description, address, active) 
VALUES ('El Buen Sabor', 'Comida criolla y pescados', 'Av. Javier Prado 1234', true);