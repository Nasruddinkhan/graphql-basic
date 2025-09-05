package com.nkhan.customer.model;

import lombok.Builder;

@Builder
public record Customer(
        String name,
        Integer customerId,
        String city,
        String state,
        String country,
        String pinCode,
        String address
) {}