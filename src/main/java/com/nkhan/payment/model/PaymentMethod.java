package com.nkhan.payment.model;

import java.time.Instant;

public sealed interface  PaymentMethod permits CreditCard, WalletDto, BankAccount{
    String id();
    String type();
    Instant createdAt();
}
