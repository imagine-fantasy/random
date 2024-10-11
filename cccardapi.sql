-- Create the schema
CREATE SCHEMA IF NOT EXISTS cc_cards;

-- Set the search path
SET search_path TO cc_cards, public;

-- Create the cardholders table
CREATE TABLE IF NOT EXISTS cardholders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    company VARCHAR(100) NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the card_creations table
CREATE TABLE IF NOT EXISTS card_creations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cardholder_id UUID NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processor_response JSONB NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (cardholder_id) REFERENCES cardholders(id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_cardholders_email ON cardholders(email);
CREATE INDEX IF NOT EXISTS idx_card_creations_cardholder_id ON card_creations(cardholder_id);
CREATE INDEX IF NOT EXISTS idx_card_creations_status ON card_creations(status);
CREATE INDEX IF NOT EXISTS idx_card_creations_date ON card_creations(request_date);

-- Create a GIN index on the JSONB column for faster JSON queries
CREATE INDEX IF NOT EXISTS idx_card_creations_response ON card_creations USING GIN (processor_response);





-- Grant privileges to read_only_role
GRANT USAGE ON SCHEMA cc_cards TO read_only_role;
GRANT SELECT ON ALL TABLES IN SCHEMA cc_cards TO read_only_role;
ALTER DEFAULT PRIVILEGES IN SCHEMA cc_cards GRANT SELECT ON TABLES TO read_only_role;

-- Grant privileges to read_write_role
GRANT USAGE, CREATE ON SCHEMA cc_cards TO read_write_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA cc_cards TO read_write_role;
ALTER DEFAULT PRIVILEGES IN SCHEMA cc_cards 
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO read_write_role;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA cc_cards TO read_only_role;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA cc_cards TO read_write_role;


GRANT CREATE ON SCHEMA cc_cards TO read_write_role;
