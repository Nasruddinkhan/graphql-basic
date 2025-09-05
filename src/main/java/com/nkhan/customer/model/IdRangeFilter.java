package com.nkhan.customer.model;

import lombok.Builder;

@Builder
public record IdRangeFilter(Integer minAge, Integer maxAge) {
}
