package com.nkhan.payment.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BankAccount( String id,
                           String type,
                           Instant createdAt,
                           String bankName,
                           String accountNumberMasked) implements  PaymentMethod{
}
