insert into users (name, email, password, role)
values
    --user111--
    ('user111', 'user1@gmail.com', '$2a$12$tDeJAlwzOdAqTdR4yO8ZGef9p9CyCDlyM/KlAuaPgjpiHxqeAIv8i', 'USER'),
    --manager111--
    ('manager111', 'manager1@gmail.com', '$2a$12$/UwPwmjAay1jIgLAA5Y5xeGCNnEkB50k3ah03irxEoQcWiz2BCvLK', 'MANAGER');

insert into products (id, name, description, price, stock_quantity)
values (gen_random_uuid(), 'Laptop', 'Laptop description', 1200.00, 50),
       (gen_random_uuid(), 'Smartphone', 'Smartphone description', 850.0, 48),
       (gen_random_uuid(), 'Headphones', 'Headphones description', 500.0, 74);