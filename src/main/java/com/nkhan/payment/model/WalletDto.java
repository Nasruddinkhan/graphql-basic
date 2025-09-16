package com.nkhan.payment.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record WalletDto( String id,
                      String type,
                      Instant createdAt,
                      double balance,
                      String currency) implements PaymentMethod {
}
