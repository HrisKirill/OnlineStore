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
    name           TEXT                                NOT NULL,
    description    TEXT,
    price          DECIMAL(10, 2)                      NOT NULL,
    stock_quantity BIGINT                              NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
