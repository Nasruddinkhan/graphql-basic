package com.nkhan.customer.service;

import com.nkhan.customer.model.CustomerOrder;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OrderService {


    Flux<CustomerOrder> getOrderByCustomerName(String name);

    Flux<List<CustomerOrder>> getOrderByCustomerName(List<String> names);
}
