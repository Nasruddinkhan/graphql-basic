package com.nkhan.customer.model;

import lombok.Builder;

@Builder
public record CustomerOrder (String orderId, String description){
}
