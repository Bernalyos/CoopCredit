-- V1__init_schema.sql
-- Initial database schema for CoopCredit

-- Affiliates table
CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    salary NUMERIC(38,2) NOT NULL,
    affiliation_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Credit Requests table
CREATE TABLE credit_requests (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    amount NUMERIC(38,2) NOT NULL,
    term INTEGER NOT NULL,
    rate NUMERIC(38,2) NOT NULL,
    request_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    risk_score INTEGER,
    risk_level VARCHAR(50),
    risk_reason VARCHAR(500),
    risk_evaluation_date TIMESTAMP,
    CONSTRAINT fk_credit_request_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates(id)
);

-- Users table (for authentication)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Indexes for better performance
CREATE INDEX idx_affiliates_document ON affiliates(document);
CREATE INDEX idx_credit_requests_affiliate_id ON credit_requests(affiliate_id);
CREATE INDEX idx_credit_requests_status ON credit_requests(status);
CREATE INDEX idx_users_username ON users(username);
