# GraphQL Basics (Spring Boot)

GraphQL in Spring Boot does not depend on your controller method names or structure directly.  
It maps your methods to queries, mutations, or field resolvers based on the **GraphQL schema (`.graphqls` file)**.

In grpahql it does not really care what you are having inside your spring controller.It loads things from the schema file.

graphql schema mapped with controller via  @QueryMapping("hello")

# Argument
In graphql we are passing argument like sayHello(name : dataType): dataType in case if you want madatory
add the  sayHello(name : dataType!)

In graphql we can call all different endpoints together.

## Query Mapping

### Using `@QueryMapping`
`@QueryMapping` is used to map **root-level queries** defined under the `type Query` in the schema.  
It is a shorthand for:
`@SchemaMapping(typeName = "Query")`
## Batch Mapping in Spring GraphQL (`@BatchMapping`)

`@BatchMapping` is used to **optimize resolving fields for a list of parent objects**.  
Instead of fetching related data individually for each parent (N+1 problem), batch mapping allows fetching all related data in a single call.

```type Customer {
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
@Controller
public class CustomerController {

    @QueryMapping
    public List<Customer> customers() {
        return customerService.getAllCustomers();
    }

    @BatchMapping(typeName = "Customer", field = "orders")
    public Map<Customer, List<Order>> orders(List<Customer> customers) {
        // Fetch orders for all customers in a single batch
        return orderService.getOrdersForCustomers(customers);
    }
}
```
# Fixing N+1 Problem & Size/Order Mismatch

When resolving nested fields like orders for multiple customers:

N+1 problem: Fetching orders individually for each customer creates one query per customer — inefficient.

@BatchMapping fetches all orders in a single batch.

Size / Order Mismatch:

Can occur if the service returns fewer lists than the number of customers.

Can occur if the mapping between parent (Customer) and child (Order) is ambiguous (duplicate names, missing keys).

To prevent mismatches, return a Map<Customer, List<Order>> where every customer has a corresponding list, even if empty:

## Data resolver
Data resolver = logic to fetch or compute a field’s value.
It does not override the field.
GraphQL automatically uses a resolver if defined, otherwise it uses the default value from the object.

## Field Alias
In GraphQL, a field alias allows you to rename a field in your query result. This is useful when:

You query the same field multiple times with different arguments, or

You want the response to have a different name than the schema field.
` account {
      id
      balance: amount
      type: accountType
    }`
## Same API call with different argument
In graphql we can call same api with different argument.
` {
firstCustomer : customerById(customerId: 102) {
name
}
secondCustomer : customerById(customerId: 101) {
name
}
}`
# Fragments
A fragment in GraphQL is a reusable set of fields that you can include in multiple queries, mutations, or subscriptions. It helps you avoid duplication and keep queries clean, modular, and maintainable.
1) To reuse common field selections across queries.
2) To reduce query duplication when the same fields are needed in different places.
3) o make queries easier to read and maintain.

`{
firstCustomer : customerById(customerId: 102) {
name
account {
...AccountFields
}
}
secCustomer : customerById(customerId: 102) {
name
account {
...AccountFields
}
}
}
fragment AccountFields on Accounts {
accountNumber: id
balance: amount
type: accountType
}
`
## Variable

A variable in GraphQL is a placeholder that allows you to pass dynamic values into a query, mutation, or subscription without hardcoding them. Variables make your GraphQL operations reusable, secure, and cleaner.

1) Avoid hardcoding values directly in queries.

2) Reuse the same query with different inputs.

3) Improve security by separating query structure from user input.

4) Reduce query complexity for dynamic values.

`query GetTwoCustomers($id1: Int!, $id2: Int!) {
firstCustomer: customerById(customerId: $id1) {
name
account {
...AccountFields
}
}
secCustomer: customerById(customerId: $id2) {
name
account {
...AccountFields
}
}
}
fragment AccountFields on Accounts {
accountNumber: id
balance: amount
type: accountType
}
`
`{ 
  "id1":101, 
  "id2":102
}`

## Directives: @include and @skip
1. @include(if: Boolean)
Includes the field only if the condition is true.

@skip(if: Boolean)
Skips the field if the condition is true.

Both take a Boolean argument and are mutually exclusive in purpose.
# @include
`query GetCustomer($id: Int!, $includeBalance: Boolean!) {
customerById(customerId: $id) {
name
account {
amount @include(if: $includeBalance)
accountType
}
}
}
`
`{
"id": 123,
"includeBalance": true
}`
#skip
`query GetCustomer($id: Int!, $skipBalance: Boolean!) {
  customerById(customerId: $id) {
    name
    account {
      amount @skip(if: $skipBalance)
      accountType
    }
  }
}
`
`{
  "id": 101,
  "skipBalance": true
}
`
## DataFetchingFieldSelectionSet

A DataFetchingFieldSelectionSet represents the set of fields requested in the current GraphQL query for a specific field or resolver. It allows you to see which fields the client has actually requested so you can:

1) Fetch only the necessary data (avoid over-fetching)

2) Perform conditional logic based on requested fields

3) Optimize database queries (e.g., JPA, Mongo, or REST calls)
   In GraphQL (especially with graphql-java and Spring Boot GraphQL), DataFetchingEnvironment (DFE) is the core object passed to your DataFetcher or Resolver method. It contains all the contextual information about the current field being resolved, including arguments, parent data, query context, and the selection set.

## DataFetchingEnvironment?

It’s an interface provided by graphql.schema that gives you access to:

Field arguments (variables provided by the client)

Parent object (source object in the resolver chain)

GraphQL context (custom context like user info, tenant ID)

Selection set (fields requested by the client)

Execution info (path, schema type, etc.)

# Key Methods in DataFetchingEnvironment

1) getArgument(String name)
Get an argument passed to the field.

String id = environment.getArgument("id");


2) getArguments()
Get all arguments as a map.

Map<String, Object> args = environment.getArguments();


3) getSource()
Get the parent object in the resolver chain.

Customer customer = environment.getSource();


4) getContext()
Access a custom context object (like JWT claims, tenant ID).

MyContext ctx = environment.getContext();


5) getSelectionSet()
Get the requested fields using DataFetchingFieldSelectionSet.

DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();


6) getFieldDefinition() / getFieldType()
Get schema metadata about the field being resolved.

7) getGraphQlContext() (in newer versions)
Access a strongly typed context using GraphQLContext.

# GraphQL Scalar

A scalar represents a single value (string, number, boolean, etc.).

They are terminal types — you can’t drill down further.

# Custom Scalars

Sometimes built-in scalars aren’t enough. You can define custom scalars for domain-specific types:

Date / DateTime , Email BigDecimal URL etc.
`@Bean
public RuntimeWiringConfigurer configurer(){
    return  c -> c.scalar(ExtendedScalars.GraphQLLong)
    .scalar(ExtendedScalars.GraphQLByte)
    .scalar(ExtendedScalars.GraphQLShort)
    .scalar(ExtendedScalars.GraphQLBigDecimal)
    .scalar(ExtendedScalars.GraphQLBigInteger)
    .scalar(ExtendedScalars.Date)
    .scalar(ExtendedScalars.GraphQLBigDecimal)
    .scalar(ExtendedScalars.LocalTime)
    .scalar(ExtendedScalars.DateTime);
}`

# GraphQL interface schema.

In GraphQL, interfaces are an abstract type that define fields that multiple object types must include. They’re useful when you want polymorphism (like in OOP) and need a way to query different object types through a common contract.

`interface PaymentMethod {
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
}`

`query PaymentMethods {
    paymentMethods(userId: "123") {
        id
        type
        createdAt
        ... on CreditCard {
            last4
        }
        ... on Wallet {
            amount: balance
            currency
        }
        ... on BankAccount {
      bankName
      accountNo: accountNumberMasked
    }
    }
}
`
# __typename
__typename is a GraphQL introspection field automatically available on all objects.

It tells you the concrete type name of the object being returned.

This is especially useful when working with interfaces and unions, where multiple types could be returned.

# TypeResolver

In GraphQL Java (and Spring GraphQL), when you return an interface (e.g. PaymentMethod) or union, the engine must know which GraphQL type name (Wallet, CreditCard, etc.) corresponds to the returned Java object.

`@Bean
    public TypeResolver typeResolver(){
        ClassNameTypeResolver resolver = new ClassNameTypeResolver();
        resolver.addMapping(WalletDto.class, "Wallet");
        return resolver;
    }`

`
@Bean
public RuntimeWiringConfigurer configurer(){
return  c -> c.type("PaymentMethod", typeWiring -> typeWiring.typeResolver(typeResolver()));
}
`

