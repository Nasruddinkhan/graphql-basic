package com.nkhan.clients;

import com.nkhan.clients.dto.CustomerDto;
import com.nkhan.clients.dto.CustomerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomerGraphQLClient {

    private final HttpGraphQlClient httpClient;
    private final WebSocketGraphQlClient wsClient;

    public CustomerGraphQLClient() {
        // 👇 Replace with your customer-ql-svc URL
        String baseUrl = "http://localhost:8082/graphql";
        String wsUrl = "ws://localhost:8082/graphql";

        this.httpClient = HttpGraphQlClient.builder()
                .url(baseUrl)
                .build();
        WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

        this.wsClient = WebSocketGraphQlClient.builder(wsUrl, webSocketClient)
                .url("ws://localhost:8082/graphql")
                .build();
    }

    // ===============================
    // 🧾 Query: Get All Customers
    // ===============================
    public Flux<CustomerDto> getAllCustomers() {
        String query = """
                query {
                  customers {
                    id
                    firstName
                    lastName
                    phone
                    createdAt
                    email
                  }
                }
                """;

        return httpClient.document(query)
                .retrieve("customers")
                .toEntityList(CustomerDto.class)
                .flatMapMany(Flux::fromIterable)
                .doOnSubscribe(s -> log.info("Fetching all customers..."))
                .doOnError(e -> log.error("Error fetching customers", e));
    }

    // ===============================
    // 🧾 Query: Get Customer by ID
    // ===============================
    public Mono<CustomerDto> getCustomerById(String id) {
        String query = """
                query ($id: ID!) {
                  customerById(id: $id) {
                    id
                    firstName
                    lastName
                    phone
                    createdAt
                    email
                  }
                }
                """;

        return httpClient.document(query)
                .variable("id", id)
                .retrieve("customerById")
                .toEntity(CustomerDto.class)
                .doOnSubscribe(s -> log.info("Fetching customer with id={}", id))
                .doOnError(e -> log.error("Error fetching customer {}", id, e));
    }

    // ===============================
    // ✏️ Mutation: Create Customer
    // ===============================
    public Mono<CustomerDto> createCustomer(CustomerDto dto) {
        String mutation = """
                mutation ($input: CustomerInput!) {
                  createCustomer(input: $input) {
                    firstName
                    lastName
                    phone
                    email
                  }
                }
                """;

        return httpClient.document(mutation)
                .variable("input", dto)
                .retrieve("createCustomer")
                .toEntity(CustomerDto.class)
                .doOnSubscribe(s -> log.info("Creating customer: {}", dto))
                .doOnError(e -> log.error("Error creating customer", e));
    }

    // ===============================
    // ✏️ Mutation: Update Customer
    // ===============================
    public Mono<CustomerDto> updateCustomer(CustomerDto dto) {
        String mutation = """
                mutation ($input: UpdateCustomerInput!) {
                  updateCustomer(input: $input) {
                    id
                    firstName
                    lastName
                    phone
                    email
                    createdAt
                  }
                }
                """;

        return httpClient.document(mutation)
                .variable("input", dto)
                .retrieve("updateCustomer")
                .toEntity(CustomerDto.class)
                .doOnSubscribe(s -> log.info("Updating customer: {}", dto))
                .doOnError(e -> log.error("Error updating customer", e));
    }

    // ===============================
    // 🗑 Mutation: Delete Customer
    // ===============================
    public Mono<Boolean> deleteCustomer(String id) {
        String mutation = """
                mutation ($id: ID!) {
                  deleteCustomer(id: $id)
                }
                """;

        return httpClient.document(mutation)
                .variable("id", id)
                .retrieve("deleteCustomer")
                .toEntity(Boolean.class)
                .doOnSubscribe(s -> log.info("Deleting customer with id={}", id))
                .doOnError(e -> log.error("Error deleting customer {}", id, e));
    }

    // ===============================
    // 🔔 Subscription: Customer Events
    // ===============================
    public Flux<CustomerEvent> subscribeToCustomerEvents() {
        String subscription = """
                subscription {
                  customerEvents {
                    id
                    msg
                  }
                }
                """;

        return wsClient.document(subscription)
                .retrieveSubscription("customerEvents")
                .toEntity(CustomerEvent.class)
                .doOnSubscribe(s -> log.info("Subscribed to customer events"))
                .doOnError(e -> log.error("Error in customer event subscription", e));
    }
}
