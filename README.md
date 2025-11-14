# 🧠 GraphQL Basics (Spring Boot)

GraphQL in Spring Boot does **not** depend on your controller structure or method names directly.  
It maps methods to **queries, mutations, or field resolvers** based on your **GraphQL schema (`.graphqls` file)**.

---

## 🗂️ Overview

GraphQL loads behavior from the **schema**, not from Spring controllers.  
Each controller method is mapped to the schema via annotations like `@QueryMapping("hello")`.

---

## ⚙️ Passing Arguments

Arguments are defined in the schema:

```graphql
sayHello(name: String): String
```

To make an argument **mandatory**, add `!`:

```graphql
sayHello(name: String!): String
```

---

## 🔄 Calling Multiple Endpoints Together

GraphQL allows you to query **multiple endpoints in a single request**, combining data efficiently.

---

## 🧩 Query Mapping

### `@QueryMapping`

Used for **root-level queries** defined under the `type Query` in your schema.  
Equivalent to:

```java
@SchemaMapping(typeName = "Query")
```

Example:

```java
@Controller
public class GreetingController {

    @QueryMapping
    public String hello() {
        return "Hello GraphQL!";
    }
}
```

---

## 🚀 Batch Mapping (`@BatchMapping`)

Used to **optimize nested data fetching** and fix the **N+1 problem**.

### Example

```graphql
type Customer {
    id: ID!
    name: String!
    orders: [Order]
}

type Order {
    orderId: ID!
    description: String
}

type Query {
    customers: [Customer]
}
```

```java
@Controller
public class CustomerController {

    @QueryMapping
    public List<Customer> customers() {
        return customerService.getAllCustomers();
    }

    @BatchMapping(typeName = "Customer", field = "orders")
    public Map<Customer, List<Order>> orders(List<Customer> customers) {
        return orderService.getOrdersForCustomers(customers);
    }
}
```

### Why `@BatchMapping`?
- Fixes **N+1 problem** (multiple DB calls per parent object)
- Fetches all child data in one batch query
- Must return `Map<Parent, List<Child>>`, even if some lists are empty

---

## 🧠 Data Resolver

A **data resolver** provides logic to fetch or compute a field’s value dynamically.  
If not defined, GraphQL uses the object’s default property value.

---

## 🧾 Field Alias

Aliases rename fields in your query result.

### Example
```graphql
account {
  id
  balance: amount
  type: accountType
}
```

You can also query the same field multiple times with different arguments:

```graphql
{
  firstCustomer: customerById(customerId: 102) { name }
  secondCustomer: customerById(customerId: 101) { name }
}
```

---

## 🧩 Fragments

Fragments help **reuse field selections** across queries, keeping them modular and maintainable.

```graphql
{
  firstCustomer: customerById(customerId: 102) {
    name
    account { ...AccountFields }
  }
  secCustomer: customerById(customerId: 101) {
    name
    account { ...AccountFields }
  }
}

fragment AccountFields on Accounts {
  accountNumber: id
  balance: amount
  type: accountType
}
```

---

## 🔢 Variables

Variables make queries **reusable and secure** by avoiding hardcoded values.

```graphql
query GetTwoCustomers($id1: Int!, $id2: Int!) {
  firstCustomer: customerById(customerId: $id1) {
    name
    account { ...AccountFields }
  }
  secCustomer: customerById(customerId: $id2) {
    name
    account { ...AccountFields }
  }
}

fragment AccountFields on Accounts {
  accountNumber: id
  balance: amount
  type: accountType
}
```

```json
{
  "id1": 101,
  "id2": 102
}
```

---

## ⚡ Directives: `@include` & `@skip`

These control conditional field inclusion.

### `@include(if: Boolean)`
Includes field **only if** condition is `true`.

### `@skip(if: Boolean)`
Skips field **if** condition is `true`.

#### Example

```graphql
query GetCustomer($id: Int!, $includeBalance: Boolean!) {
  customerById(customerId: $id) {
    name
    account {
      amount @include(if: $includeBalance)
      accountType
    }
  }
}
```

```json
{
  "id": 123,
  "includeBalance": true
}
```

---

## 🧱 DataFetchingFieldSelectionSet

Represents the **set of fields** requested in the query for a specific resolver.  
Used to:
- Fetch only necessary fields
- Avoid over-fetching
- Optimize DB or REST calls

---

## 🧰 DataFetchingEnvironment

Provides access to query context, arguments, and metadata.

### Common Methods

| Method | Description |
|--------|--------------|
| `getArgument(String name)` | Get a single argument |
| `getArguments()` | Get all arguments as a map |
| `getSource()` | Get parent object |
| `getContext()` | Access custom context (e.g., JWT, tenant ID) |
| `getSelectionSet()` | Get requested fields |
| `getFieldDefinition()` / `getFieldType()` | Schema metadata |
| `getGraphQlContext()` | Typed context (newer versions) |

---

## 🔢 GraphQL Scalars

Scalars represent single values (e.g., `String`, `Int`, `Float`, `Boolean`, `ID`).

### Custom Scalars Example

```java
@Bean
public RuntimeWiringConfigurer configurer() {
    return c -> c
        .scalar(ExtendedScalars.GraphQLLong)
        .scalar(ExtendedScalars.GraphQLBigDecimal)
        .scalar(ExtendedScalars.Date)
        .scalar(ExtendedScalars.DateTime)
        .scalar(ExtendedScalars.LocalTime);
}
```

---

## 🧬 Interfaces in GraphQL

Interfaces define shared fields that multiple object types must implement.

```graphql
interface PaymentMethod {
  id: ID!
  type: String!
  createdAt: String!
}

type CreditCard implements PaymentMethod {
  id: ID!
  type: String!
  createdAt: String!
  last4: String!
  brand: String!
}

type BankAccount implements PaymentMethod {
  id: ID!
  type: String!
  createdAt: String!
  bankName: String!
  accountNumberMasked: String!
}

type Wallet implements PaymentMethod {
  id: ID!
  type: String!
  createdAt: String!
  balance: Float!
  currency: String!
}

type Query {
  paymentMethods(userId: ID!): [PaymentMethod!]!
}
```

### Query Example

```graphql
query PaymentMethods {
  paymentMethods(userId: "123") {
    id
    type
    createdAt
    ... on CreditCard { last4 }
    ... on Wallet { amount: balance, currency }
    ... on BankAccount { bankName, accountNo: accountNumberMasked }
  }
}
```

---

## 🧩 `__typename`

`__typename` is a **built-in introspection field** that reveals the object’s concrete GraphQL type.  
Useful when working with **interfaces** or **unions**.

---

## 🧠 TypeResolver in Spring Boot

Spring GraphQL requires a `TypeResolver` to map Java DTOs to GraphQL types.

```java
@Bean
public TypeResolver typeResolver() {
    ClassNameTypeResolver resolver = new ClassNameTypeResolver();
    resolver.addMapping(WalletDto.class, "Wallet");
    return resolver;
}

@Bean
public RuntimeWiringConfigurer configurer() {
    return c -> c.type("PaymentMethod", 
        typeWiring -> typeWiring.typeResolver(typeResolver()));
}
```
---

## 🚀 GraphQL Query Caching with `PreparsedDocumentProvider`

By default, every GraphQL query sent to the server is **parsed and validated** before execution.  
For large or frequently repeated queries, this process can become expensive.  
To optimize this, Spring Boot allows caching parsed GraphQL documents using a `PreparsedDocumentProvider`.

### 🧠 What It Does

- Caches parsed GraphQL documents to avoid re-parsing on repeated queries
- Improves performance for repeated or large client queries
- Reduces CPU load and validation time
- Fully supports asynchronous execution

---

### 🧩 Implementation Example

#### 📁 **Configuration Class**

```java
package com.nkhan.cache.config;

import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class OperationalCachingConfig {

    @Bean
    public GraphQlSourceBuilderCustomizer sourceBuilderCustomizer(PreparsedDocumentProvider provider) {
        return builder -> builder.configureGraphQl(configurer ->
                configurer.preparsedDocumentProvider(provider));
    }

    @Bean
    public PreparsedDocumentProvider preparsedDocumentProvider() {
        Map<String, PreparsedDocumentEntry> cache = new ConcurrentHashMap<>();

        return (executionInput, parseAndValidateFunction) ->
                CompletableFuture.supplyAsync(() ->
                        cache.computeIfAbsent(executionInput.getQuery(), query -> {
                            log.debug("Cache miss for GraphQL query. Parsing and validating...");
                            PreparsedDocumentEntry entry = parseAndValidateFunction.apply(executionInput);
                            log.debug("Query cached successfully: {}", query);
                            return entry;
                        })
                );
    }
}
```

---

### ⚙️ Example Controller

```java
package com.nkhan.cache.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class OperationalCacheController {

    @QueryMapping("getDataFromOperationalCache")
    public Mono<String> getOperation(@Argument("name") String name) {
        log.info("Received request with name = {}", name);
        return Mono.fromSupplier(() ->
                "Hello, %s! Now it's working.".formatted(name)
        );
    }
}
```

---

### 🧬 GraphQL Schema Definition

```graphql
extend type Query {
    getDataFromOperationalCache(name: String!): String
}
```

---

### 🧪 Example Query

```graphql
query {
  getDataFromOperationalCache(name: "Nasruddin")
}
```

### ✅ Expected Response

```json
{
  "data": {
    "getDataFromOperationalCache": "Hello, Nasruddin! Now it's working."
  }
}
```
---


## ⚙️ Client Initialization

``` java
private final HttpGraphQlClient httpClient;
private final WebSocketGraphQlClient wsClient;

public CustomerGraphQLClient() {
    String httpUrl = "http://localhost:8082/graphql";
    String wsUrl   = "ws://localhost:8082/graphql";

    this.httpClient = HttpGraphQlClient.builder()
            .url(httpUrl)
            .build();

    WebSocketClient socket = new ReactorNettyWebSocketClient();

    this.wsClient = WebSocketGraphQlClient.builder(wsUrl, socket)
            .build();
}
```

------------------------------------------------------------------------

## 🔍 Queries

### Get All Customers

``` java
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
            .flatMapMany(Flux::fromIterable);
}
```

### Get Customer by ID

``` java
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
```

✅ **Summary**

| Concept                 | Purpose                                                                       |
|-------------------------|-------------------------------------------------------------------------------|
| `@QueryMapping`         | Root-level queries                                                            |
| `@BatchMapping`         | Fix N+1 problem                                                               |
| Field Alias             | Rename result fields                                                          |
| Fragment                | Reuse query fields                                                            |
| Variable                | Parameterize queries                                                          |
| Directive               | Conditional fields                                                            |
| DataFetchingEnvironment | Query context & metadata                                                      |
| Custom Scalar           | Extend GraphQL data types                                                     |
| Interface               | Shared fields for multiple types                                              |
| TypeResolver            | Resolve interface/union types                                                 |
| GrpahQL Clinet          | https://github.com/Nasruddinkhan/customer-ql-svc-mongo Consume graphql client |
