package com.nkhan.accounts.controller;

import com.nkhan.accounts.model.AccountType;
import com.nkhan.accounts.model.Accounts;
import com.nkhan.customer.model.Customer;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class AccountsController {
    @SchemaMapping(typeName = "Customer",  field = "account")
    public Mono<Accounts> getAccounts(Customer customer) {
        final AccountType type =
                ThreadLocalRandom.current().nextBoolean() ? AccountType.CURRENT : AccountType.SAVING;
        return Mono.just(Accounts.builder()
                        .id(UUID.randomUUID())
                        .accountType(type)
                        .amount(ThreadLocalRandom.current().nextInt(100, 10000))
                .build());
    }
}
