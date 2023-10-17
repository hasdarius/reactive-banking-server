CREATE TABLE account (
                         id SERIAL PRIMARY KEY,
                         account_number VARCHAR(255) UNIQUE NOT NULL,
                         total_balance NUMERIC(10, 2)
);

CREATE INDEX idx_account_accountNumber
    ON account (account_number);

CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              account_number VARCHAR(255) REFERENCES account(account_number),
                              date DATE NOT NULL,
                              transaction_details VARCHAR(255),
                              value_date DATE NOT NULL,
                              withdrawal_amount NUMERIC(10, 2),
                              deposit_amount NUMERIC(10, 2)
);
