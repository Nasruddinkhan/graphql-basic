package com.nkhan.customer.service.impl;

import com.nkhan.customer.model.CustomerOrder;
import com.nkhan.customer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OderServiceImpl implements OrderService {
    private final   Map<String, List<CustomerOrder>> orders = Map.of("John Doe",
            List.of(
                    CustomerOrder.builder().orderId(UUID.randomUUID().toString()).description("IPhone-16").build(),
                    CustomerOrder.builder().orderId(UUID.randomUUID().toString()).description("Mac-book-Pro 16").build()),
            "Jane Smith",
            List.of(
                    CustomerOrder.builder().orderId(UUID.randomUUID().toString()).description("IPhone-16").build(),
                    CustomerOrder.builder().orderId(UUID.randomUUID().toString()).description("Mac-book-Pro 16").build())
    );
    @Override
    public Flux<CustomerOrder> getOrderByCustomerName(String name) {
        return Flux.fromIterable(orders.getOrDefault(name, List.of()));
    }

    @Override
    public Flux<List<CustomerOrder>> getOrderByCustomerName(List<String> names) {
        return Flux.fromIterable(names).map(name-> orders.getOrDefault(name, List.of()));
    }
}
