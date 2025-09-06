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
