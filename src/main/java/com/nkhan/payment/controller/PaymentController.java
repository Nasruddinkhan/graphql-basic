package com.nkhan.payment.controller;

import com.nkhan.payment.model.BankAccount;
import com.nkhan.payment.model.CreditCard;
import com.nkhan.payment.model.PaymentMethod;
import com.nkhan.payment.model.WalletDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Controller
@Slf4j
public class PaymentController {
    @QueryMapping
    public Flux<PaymentMethod> paymentMethods(@Argument String userId) {
        log.info("userId = {}", userId);
        // Normally, you’d fetch from DB or external service
        return Flux.just(
                CreditCard.builder()
                        .id("cc-123")
                        .type("CREDIT_CARD")
                        .createdAt(Instant.now())
                        .last4("4242")
                        .brand("Visa")
                        .build(),
                BankAccount.builder()
                        .id("ba-456")
                        .type("BANK_ACCOUNT")
                        .createdAt(Instant.now())
                        .bankName("Chase")
                        .accountNumberMasked("****5678")
                        .build(),
                WalletDto.builder()
                        .id("wa-789")
                        .type("WALLET")
                        .createdAt(Instant.now())
                        .balance(150.75)
                        .currency("USD")
                        .build());
    }
}
