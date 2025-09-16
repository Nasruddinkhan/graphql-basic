package com.nkhan.payment.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CreditCard(String id,
                         String type,
                         Instant createdAt,
                         String last4,
                         String brand) implements PaymentMethod {
}
