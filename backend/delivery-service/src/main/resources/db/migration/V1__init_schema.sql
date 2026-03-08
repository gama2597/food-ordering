CREATE TABLE deliveries (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    customer_auth_user_id VARCHAR(120) NOT NULL,
    status VARCHAR(20) NOT NULL,
    assigned_at TIMESTAMP,
    started_at TIMESTAMP,
    delivered_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_deliveries_order_id UNIQUE (order_id)
);

CREATE INDEX idx_deliveries_order_id ON deliveries (order_id);
