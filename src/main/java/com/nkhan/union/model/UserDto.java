package com.nkhan.union.model;

import lombok.Builder;

@Builder
public record UserDto( String id,
                       String username,
                       String email) implements SearchResult {
}
