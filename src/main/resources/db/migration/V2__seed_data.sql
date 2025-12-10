-- V2__seed_data.sql
-- Initial seed data for CoopCredit

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, role) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN');

-- Insert sample analyst user (password: analyst123)
INSERT INTO users (username, password, role) VALUES 
('analyst', '$2a$10$DowJonesRZW.UrBs.Nt.aeO7p3IfRcdXJFLIy9FJiL4qRKqJKqKKe', 'ROLE_ANALISTA');

-- Insert sample affiliates
INSERT INTO affiliates (document, name, salary, affiliation_date, status) VALUES
('12345678', 'Juan Pérez', 5000.00, '2023-01-15', 'ACTIVE'),
('87654321', 'María García', 6500.00, '2023-03-20', 'ACTIVE'),
('11223344', 'Carlos López', 4500.00, '2023-06-10', 'ACTIVE');
