package com.nkhan.union.model;

public sealed interface SearchResult permits ArticleDto, ProductDto, UserDto {
}
