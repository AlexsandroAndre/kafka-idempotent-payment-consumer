CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Contas financeiras
CREATE TABLE accounts (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          account_number VARCHAR(32) NOT NULL UNIQUE,
                          holder_name VARCHAR(120) NOT NULL,
                          currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
                          balance NUMERIC(19, 4) NOT NULL DEFAULT 0.0000,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          CONSTRAINT chk_positive_balance CHECK (balance >= 0)
);

-- 2. Tabela de Idempotência Estrita
CREATE TABLE processed_events (
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                  idempotency_key VARCHAR(128) NOT NULL UNIQUE,
                                  event_id VARCHAR(128) NOT NULL,
                                  transaction_id VARCHAR(128) NOT NULL,
                                  payload_hash VARCHAR(64) NOT NULL,
                                  status VARCHAR(32) NOT NULL,
                                  processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_processed_events_key ON processed_events(idempotency_key);

-- 3. Livro-Razão (Ledger) imutável
CREATE TABLE financial_transactions (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        transaction_id VARCHAR(128) NOT NULL,
                                        account_id UUID NOT NULL REFERENCES accounts(id),
                                        amount NUMERIC(19, 4) NOT NULL,
                                        currency VARCHAR(3) NOT NULL,
                                        transaction_type VARCHAR(32) NOT NULL,
                                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                        CONSTRAINT chk_amount_positive CHECK (amount > 0)
);

CREATE INDEX idx_financial_transactions_account ON financial_transactions(account_id);

-- Conta pré-cadastrada para teste
INSERT INTO accounts (id, account_number, holder_name, currency, balance)
VALUES ('a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 'ACC-9988-1', 'Alice Nakamoto Corp', 'BRL', 1000.0000);