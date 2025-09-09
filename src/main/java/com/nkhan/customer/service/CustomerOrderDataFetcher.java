package com.nkhan.customer.service;

import com.nkhan.customer.model.Customer;
import com.nkhan.customer.model.CustomerWithOrder;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class CustomerOrderDataFetcher {

    private final CustomerService customerService;
    private final OrderService orderService;

    public Flux<CustomerWithOrder> getAllCustomer(DataFetchingFieldSelectionSet selectionSet){
        var isOrder = selectionSet.contains("orders");
        return customerService.findAllCustomers()
                .map(c->
                        CustomerWithOrder.builder()
                                .name(c.name())
                                .customerId(c.customerId())
                                .city(c.city())
                                .state(c.state())
                                .country(c.country())
                                .pinCode(c.pinCode())
                                .address(c.address())
                                .orders(Collections.emptyList())
                                .build())
                .transform(updateOrder(isOrder));
    }

    private UnaryOperator<Flux<CustomerWithOrder>> updateOrder(boolean isOrder) {
        return isOrder ? f -> f.flatMap(this::getOrder) : f -> f;
    }
    private Mono<CustomerWithOrder> getOrder(CustomerWithOrder customerWithOrder){
        return this.orderService.getOrderByCustomerName(customerWithOrder.name())
                .collectList()
                .map(customerWithOrder::withOrders) // replaces with NEW record instance
                .log();

    }
}
