package com.nkhan.customer.service;

import com.nkhan.customer.model.CustomerOrder;
import reactor.core.publisher.Flux;

public interface OrderService {


    Flux<CustomerOrder> getOrderByCustomerName(String name);

}
