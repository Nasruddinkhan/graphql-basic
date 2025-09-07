package com.nkhan.accounts.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Accounts(
        UUID id,
        Integer amount,
        AccountType accountType
) {}