CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT                                NOT NULL,
    email      TEXT UNIQUE                         NOT NULL,
    password   TEXT                                NOT NULL,
    role       TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE products
(
    id             UUID PRIMARY KEY,
    name           TEXT UNIQUE                         NOT NULL,
    description    TEXT,
    price          DECIMAL(10, 2)                      NOT NULL,
    stock_quantity INT                                 NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE orders
(
    id         UUID PRIMARY KEY,
    owner_id   BIGSERIAL      NOT NULL,
    status     TEXT           NOT NULL,
    created_at TIMESTAMP               DEFAULT CURRENT_TIMESTAMP NOT NULL,
    total      DECIMAL(11, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE order_items
(
    id         BIGSERIAL PRIMARY KEY,
    order_id   UUID      NOT NULL,
    product_id UUID      NOT NULL,
    quantity   BIGSERIAL NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);