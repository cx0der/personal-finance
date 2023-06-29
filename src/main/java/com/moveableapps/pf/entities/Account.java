package com.moveableapps.pf.entities;

import java.time.Instant;
import java.util.Date;
import java.util.OptionalLong;

public record Account(OptionalLong id, String name, String description, String currency, AccountType type,
                      Date createdAt, Date updatedAt) {
    public Account(String name, String description, String currency, AccountType type) {
        this(OptionalLong.empty(), name, description, currency, type, Date.from(Instant.now()), Date.from(Instant.now()));
    }

    public Account copyWithAccountId(long accountId) {
        return new Account(OptionalLong.of(accountId), this.name, this.description, this.currency, this.type, this.createdAt, this.updatedAt);
    }
}
