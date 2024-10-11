-- Drop tables first (to avoid dependency issues)
DROP TABLE IF EXISTS cc_cards.card_creations;
DROP TABLE IF EXISTS cc_cards.cardholders;

-- Drop the schema
DROP SCHEMA IF EXISTS cc_cards CASCADE;
