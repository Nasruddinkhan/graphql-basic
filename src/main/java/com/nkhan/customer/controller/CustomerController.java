package com.nkhan.customer.controller;


import com.nkhan.customer.model.Customer;
import com.nkhan.customer.model.IdRangeFilter;
import com.nkhan.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @QueryMapping("customers")
    public Flux<Customer> findAllCustomer(){
        return customerService.findAllCustomers().log();
    }

    @QueryMapping("customerByAddressContain")
    public Flux<Customer> findAllCustomer(@Argument String address){
        return customerService.customerByAddressContain(address).log();
    }

    @QueryMapping("customerById")
    public Mono<Customer> findCustomerById(@Argument Integer customerId){
        return customerService.findCustomerById(customerId).log();
    }
    @QueryMapping("customerByRangeId")
    public Flux<Customer> findCustomerByIdRange(@Argument IdRangeFilter idFilter){
        return customerService.findCustomerByIdRange(idFilter).log();
    }
}
