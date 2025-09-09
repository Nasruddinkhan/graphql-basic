package com.nkhan.customer.service.datafetcher;

import com.nkhan.customer.model.CustomerWithOrder;
import com.nkhan.customer.service.CustomerService;
import com.nkhan.customer.service.OrderService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.function.UnaryOperator;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDataFetcher implements DataFetcher<Flux<CustomerWithOrder>> {
    private final CustomerService customerService;
    private final OrderService orderService;


    @Override
    public Flux<CustomerWithOrder> get(DataFetchingEnvironment environment) throws Exception {
        var isOrder = environment.getSelectionSet().contains("orders");
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
