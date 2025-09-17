package com.nkhan.union.model;

import lombok.Builder;

@Builder
public record ProductDto(  String id,
                           String name,
                           Double price) implements SearchResult{
}
