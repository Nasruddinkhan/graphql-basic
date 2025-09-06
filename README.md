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
```java
@SchemaMapping(typeName = "Query")
## Batch Mapping in Spring GraphQL (`@BatchMapping`)

`@BatchMapping` is used to **optimize resolving fields for a list of parent objects**.  
Instead of fetching related data individually for each parent (N+1 problem), batch mapping allows fetching all related data in a single call.

---

### **Example Schema**

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
