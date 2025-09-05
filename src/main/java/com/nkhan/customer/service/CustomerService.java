package com.nkhan.customer.service;

import com.nkhan.customer.model.Customer;
import com.nkhan.customer.model.IdRangeFilter;
import org.apache.logging.log4j.LogBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> findAllCustomers();
    Flux<Customer> customerByAddressContain(String address);

    Mono<Customer> findCustomerById(Integer customerId);

    Flux<Customer> findCustomerByIdRange(IdRangeFilter idFilter);
}
