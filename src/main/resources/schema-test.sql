CREATE TABLE IF NOT EXISTS user_account
(
    user_id   UUID PRIMARY KEY,
    email     TEXT UNIQUE NOT NULL,
    password  TEXT        NOT NULL,
    firstname TEXT        NOT NULL,
    lastname  TEXT        NOT NULL,
    username  TEXT UNIQUE NOT NULL,
    role      VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS relation
(
    user_id_1  UUID REFERENCES user_account (user_id),
    user_id_2  UUID REFERENCES user_account (user_id),
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id_1, user_id_2)
);

CREATE TABLE IF NOT EXISTS transaction
(
    transaction_id   UUID PRIMARY KEY,
    description      TEXT       NOT NULL,
    amount           NUMERIC    NOT NULL,
    currency         VARCHAR(3) NOT NULL,
    transaction_date TIMESTAMP  NOT NULL
);

CREATE TABLE IF NOT EXISTS transfer
(
    from_user_id   UUID REFERENCES user_account (user_id),
    to_user_id     UUID REFERENCES user_account (user_id),
    transaction_id UUID REFERENCES transaction (transaction_id),
    PRIMARY KEY (from_user_id, to_user_id, transaction_id)
);

CREATE TABLE IF NOT EXISTS bank_account
(
    bank_account_id UUID PRIMARY KEY,
    user_id         UUID        NOT NULL REFERENCES user_account (user_id),
    iban            TEXT UNIQUE NOT NULL,
    country         TEXT        NOT NULL
);

CREATE TABLE IF NOT EXISTS bank_transaction
(
    bank_transaction_id UUID PRIMARY KEY,
    bank_account_id     UUID       NOT NULL REFERENCES bank_account (bank_account_id),
    amount              NUMERIC    NOT NULL,
    currency            VARCHAR(3) NOT NULL,
    date                TIMESTAMP  NOT NULL
);

CREATE TABLE IF NOT EXISTS balance_by_currency
(
    balance_id UUID PRIMARY KEY,
    user_id    UUID       NOT NULL REFERENCES user_account (user_id),
    balance    NUMERIC,
    currency   VARCHAR(3) NOT NULL
);

CREATE TABLE IF NOT EXISTS pay_my_buddy_deduction
(
    deduction_id   UUID PRIMARY KEY,
    transaction_id UUID    NOT NULL REFERENCES transaction (transaction_id),
    amount         NUMERIC NOT NULL
);
