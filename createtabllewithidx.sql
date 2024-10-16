-- Create the cardholders table
CREATE TABLE IF NOT EXISTS cc_cards.cardholders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    company VARCHAR(100) NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_cardholders_email ON cc_cards.cardholders(email);
CREATE INDEX IF NOT EXISTS idx_cardholders_created_at ON cc_cards.cardholders(created_at);

-- Create the card_creations table
CREATE TABLE IF NOT EXISTS cc_cards.card_creations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cardholder_id UUID NOT NULL,
    request_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    processor_response JSONB NOT NULL,
    status VARCHAR(20) NOT NULL,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    FOREIGN KEY (cardholder_id) REFERENCES cc_cards.cardholders(id)
);

CREATE INDEX IF NOT EXISTS idx_card_creations_cardholder_id ON cc_cards.card_creations(cardholder_id);
CREATE INDEX IF NOT EXISTS idx_card_creations_card_number ON cc_cards.card_creations(card_number);
CREATE INDEX IF NOT EXISTS idx_card_creations_status ON cc_cards.card_creations(status);

CREATE TABLE IF NOT EXISTS cc_cards.card_form_factors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    card_creation_id UUID NOT NULL,
    form_factor VARCHAR(20) NOT NULL,
    FOREIGN KEY (card_creation_id) REFERENCES cc_cards.card_creations(id)
);

CREATE INDEX IF NOT EXISTS idx_card_form_factors_card_creation_id ON cc_cards.card_form_factors(card_creation_id);

    
