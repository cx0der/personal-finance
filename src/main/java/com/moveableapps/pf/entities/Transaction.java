package com.moveableapps.pf.entities;

import java.util.Date;
import java.util.OptionalLong;

public record Transaction(Date postDate, Date entryDate, String description, OptionalLong id) {
    public Transaction(Date postDate, Date entryDate, String description) {
        this(postDate, entryDate, description, OptionalLong.empty());
    }

    public Transaction copyWithTransactionId(long transactionId) {
        return new Transaction(this.postDate, this.entryDate, this.description, OptionalLong.of(transactionId));
    }
}
