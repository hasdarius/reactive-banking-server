CREATE TABLE account (
                         id SERIAL PRIMARY KEY,
                         account_number VARCHAR(255) UNIQUE NOT NULL,
                         account_holder VARCHAR(255) NOT NULL
);

CREATE TABLE transaction (
                              id SERIAL PRIMARY KEY,
                              account_number VARCHAR(255) REFERENCES account(account_number),
                              date DATE NOT NULL,
                              transaction_details VARCHAR(255),
                              processed_date DATE NOT NULL,
                              withdrawal_amount NUMERIC(10, 2),
                              deposit_amount NUMERIC(10, 2)
);

CREATE INDEX idx_account_accountNumber
    ON account (account_number);

CREATE INDEX idx_account_accountHolder
    ON account (account_holder);

CREATE INDEX idx_transactions_date
    ON transaction (date);

CREATE INDEX idx_transactions_accountNumber
    ON transaction (account_number);

INSERT INTO account (account_number, account_holder) VALUES ('409000611074', 'Darius Has');