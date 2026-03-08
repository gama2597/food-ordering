CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    auth_user_id VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    phone VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label VARCHAR(50) NOT NULL,
    line_1 VARCHAR(200) NOT NULL,
    line_2 VARCHAR(200),
    district VARCHAR(80) NOT NULL,
    city VARCHAR(80) NOT NULL,
    reference VARCHAR(255),
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_user_addresses_user
        FOREIGN KEY (user_id) REFERENCES user_profiles (id) ON DELETE CASCADE
);

CREATE INDEX idx_user_profiles_auth_user_id ON user_profiles (auth_user_id);
CREATE INDEX idx_user_addresses_user_id ON user_addresses (user_id);
