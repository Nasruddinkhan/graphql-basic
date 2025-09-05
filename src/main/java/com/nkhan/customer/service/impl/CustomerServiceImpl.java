package com.nkhan.customer.service.impl;

import com.nkhan.customer.model.Customer;
import com.nkhan.customer.model.IdRangeFilter;
import com.nkhan.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

import static com.nkhan.util.RandomStringGenerator.randomString;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final Flux<Customer> customerFlux = Flux.just(
            Customer.builder()
                    .customerId(ThreadLocalRandom.current().nextInt(0, 1000))
                    .name(randomString(20))

                    .state(randomString(20))
                    .city(randomString(15))
                    .country(randomString(10))
                    .address(randomString(500))
                    .pinCode(randomString(6))
                    .address(randomString(2000))
            .build(),
            Customer.builder()
                    .customerId(ThreadLocalRandom.current().nextInt(0, 1000))
                    .name(randomString(20))

                    .state(randomString(20))
                    .city(randomString(15))
                    .country(randomString(10))
                    .address(randomString(500))
                    .pinCode(randomString(6))
                    .address(randomString(2000))
                    .build(),
            Customer.builder()
                    .name(randomString(20))

                    .customerId(ThreadLocalRandom.current().nextInt(0, 1000))
                    .state(randomString(20))
                    .city(randomString(15))
                    .country(randomString(10))
                    .address(randomString(500))
                    .pinCode(randomString(6))
                    .address(randomString(2000))
                    .build(),
            Customer.builder()
                    .customerId(ThreadLocalRandom.current().nextInt(0, 1000))
                    .state(randomString(20))
                    .city(randomString(15))
                    .country(randomString(10))
                    .address(randomString(500))
                    .pinCode(randomString(6))
                    .address(randomString(2000))
                    .build(),
            Customer.builder()
                    .name("John Doe")
                    .customerId(101)
                    .city("Riyadh")
                    .state("Riyadh")
                    .country("Saudi Arabia")
                    .pinCode("11564")
                    .address("King Fahd Road 123")
                    .build(),

            Customer.builder()
                    .name("Jane Smith")
                    .customerId(102)
                    .city("Jeddah")
                    .state("Makkah")
                    .country("Saudi Arabia")
                    .pinCode("21433")
                    .address("Al Hamra Street 56")
                    .build(),

            Customer.builder()
                    .name("Ali Khan")
                    .customerId(103)
                    .city("Dammam")
                    .state("Eastern Province")
                    .country("Saudi Arabia")
                    .pinCode("32241")
                    .address("Prince Nayef Road 45")
                    .build());
    @Override
    public Flux<Customer> findAllCustomers() {
        return customerFlux;
    }

    @Override
    public Flux<Customer> customerByAddressContain(String address) {
        return customerFlux.filter(customer -> customer.address().contains(address));
    }

    @Override
    public Mono<Customer> findCustomerById(Integer customerId) {
        return customerFlux.filter(e-> e.customerId().equals(customerId))
                .next();
    }

    @Override
    public Flux<Customer> findCustomerByIdRange(IdRangeFilter idFilter) {
        return customerFlux.filter(e-> e.customerId() >= idFilter.minAge()
            && e.customerId() <= idFilter.maxAge()
        );
    }
}
