package com.nkhan.customer.model;

import lombok.Builder;

import java.util.List;
@Builder
public record CustomerWithOrder(
        String name,
        Integer customerId,
        String city,
        String state,
        String country,
        String pinCode,
        String address,
        List<CustomerOrder> orders
) {
    public CustomerWithOrder withOrders(List<CustomerOrder> newOrders) {
        return new CustomerWithOrder(
                this.name,
                this.customerId,
                this.city,
                this.state,
                this.country,
                this.pinCode,
                this.address,
                newOrders
        );
    }
}
