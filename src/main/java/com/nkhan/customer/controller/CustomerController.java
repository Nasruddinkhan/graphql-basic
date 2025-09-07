package com.nkhan.customer.controller;


import com.nkhan.customer.model.Customer;
import com.nkhan.customer.model.CustomerOrder;
import com.nkhan.customer.model.IdRangeFilter;
import com.nkhan.customer.service.CustomerService;
import com.nkhan.customer.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;

    @SchemaMapping(
            typeName = "Query",
            field = "customers"
    )
    // @QueryMapping("customers")
    public Flux<Customer> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    @QueryMapping("customerByAddressContain")
    public Flux<Customer> findAllCustomer(@Argument String address) {
        return customerService.customerByAddressContain(address).log();
    }

    @QueryMapping("customerById")
    public Mono<Customer> findCustomerById(@Argument Integer customerId) {
        return customerService.findCustomerById(customerId).log();
    }

    @QueryMapping("customerByRangeId")
    public Flux<Customer> findCustomerByIdRange(@Argument IdRangeFilter idFilter) {
        return customerService.findCustomerByIdRange(idFilter).log();
    }

    /* N+1 problem
       //@SchemaMapping(typeName = "Customer",  field = "orders")
       @BatchMapping(typeName = "Customer",  field = "orders")
       //@QueryMapping("orders")
       public  Flux<List<CustomerOrder>>  getOrderByCustomerName(List<Customer> customer){
           return orderService.getOrderByCustomerName(customer.stream().map(Customer::name).toList());
       }
   */
    //N+1 problem with order Size/Order Mismatch
    @BatchMapping(typeName = "Customer", field = "orders")
    public Mono<Map<Customer, List<CustomerOrder>>> getOrders(List<Customer> customers) {
        var names = customers.stream().map(Customer::name).toList();
        return orderService.getOrderByCustomerName(names)
                .collectList() // Flux<List<CustomerOrder>> → List<List<CustomerOrder>>
                .map(orderLists ->
                        IntStream.range(0, customers.size())
                                .boxed()
                                .collect(Collectors.toMap(
                                        customers::get, // key: Customer
                                        i -> i < orderLists.size() ? orderLists.get(i) : List.of() // value: orders or empty list
                                ))
                );
    }
}
