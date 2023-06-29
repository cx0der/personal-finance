package com.moveableapps.pf.entities;

import java.util.OptionalLong;

public record AutoMapping(String description, Long accountId, OptionalLong id) {

    public AutoMapping(String description, long accountId) {
        this(description, accountId, OptionalLong.empty());
    }

    public AutoMapping copyWithMappingId(long mappingId) {
        return new AutoMapping(description, accountId, OptionalLong.of(mappingId));
    }
}
