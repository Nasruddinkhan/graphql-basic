package com.nkhan.clients.controller;

import com.nkhan.clients.CustomerGraphQLClient;
import com.nkhan.clients.dto.CustomerDto;
import com.nkhan.clients.dto.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.PublicKey;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CustomerClientController {

    private final CustomerGraphQLClient graphQLClient;

    @QueryMapping("clientCustomers")
    public Flux<CustomerDto> findAllCustomer(){
        log.info("customer.client called");
        return graphQLClient.getAllCustomers();
    }

    @QueryMapping("clientCustomerById")
    public Mono<CustomerDto> findByCustomerId(@Argument("id") String customerId){
        log.info("customer.client findByCustomerId");
        return graphQLClient.getCustomerById(customerId);
    }

    @MutationMapping("clientCreateCustomer")
    public Mono<CustomerDto> createCustomer(@Argument("input") CustomerDto customer){
        return graphQLClient.createCustomer(customer);
    }
  @MutationMapping("updateClientCustomer")
    public Mono<CustomerDto> upDate(@Argument("input") CustomerDto customer){
        return graphQLClient.updateCustomer(customer);
    }

    @MutationMapping("deleteClientCustomer")
    public Mono<Boolean> deleteCustomer(@Argument("id") String id){
        return graphQLClient.deleteCustomer(id);
    }


    @SubscriptionMapping("clientCustomerEvents")
    public Flux<CustomerEvent> customerEvents() {
        return graphQLClient.subscribeToCustomerEvents();
    }
}
