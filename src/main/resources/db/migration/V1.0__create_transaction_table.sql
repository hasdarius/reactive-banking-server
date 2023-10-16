CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              account_number VARCHAR(255) NOT NULL,
                              date DATE NOT NULL,
                              transaction_details VARCHAR(255),
                              value_date DATE NOT NULL,
                              withdrawal_amount NUMERIC(10, 2),
                              deposit_amount NUMERIC(10, 2),
                              balance_amount NUMERIC(10, 2)
);
