package com.moveableapps.pf.entities;

import java.util.Date;
import java.util.Optional;
import java.util.OptionalLong;

public record Split(long accountId, char isReconciled, long valueNum, int valueDenom, Optional<Date> reconciledAt,
                    OptionalLong txnId, OptionalLong splitId) {
    public Split(long accountId, char isReconciled, long valueNum, int valueDenom) {
        this(accountId, isReconciled, valueNum, valueDenom, Optional.empty(), OptionalLong.empty(), OptionalLong.empty());
    }

    public Split(long accountId, char isReconciled, Date reconciledAt, long valueNum, int valueDenom) {
        this(accountId, isReconciled, valueNum, valueDenom, Optional.of(reconciledAt), OptionalLong.empty(), OptionalLong.empty());
    }

    public Split copyWithSplitId(long splitId) {
        return new Split(accountId, isReconciled, valueNum, valueDenom, reconciledAt, txnId, OptionalLong.of(splitId));
    }

    public Split copyWithTxnId(long txnId) {
        return new Split(accountId, isReconciled, valueNum, valueDenom, reconciledAt, OptionalLong.of(txnId), splitId);
    }
}
