CREATE TABLE IF NOT EXISTS account (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    currency TEXT NOT NULL,
    type TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS txn (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    post_date TIMESTAMP NOT NULL,
    entry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS split(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER NOT NULL,
    txn_id INTEGER NOT NULL,
    is_reconciled CHAR(1) NOT NULL DEFAULT 'n',
    reconciled_at TIMESTAMP,
    value_num INTEGER NOT NULL,
    value_denom INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS auto_mapping(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL,
    account_id INTEGER NOT NULL
);