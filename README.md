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

✅ **Summary**

| Concept | Purpose |
|----------|----------|
| `@QueryMapping` | Root-level queries |
| `@BatchMapping` | Fix N+1 problem |
| Field Alias | Rename result fields |
| Fragment | Reuse query fields |
| Variable | Parameterize queries |
| Directive | Conditional fields |
| DataFetchingEnvironment | Query context & metadata |
| Custom Scalar | Extend GraphQL data types |
| Interface | Shared fields for multiple types |
| TypeResolver | Resolve interface/union types |
