package com.nkhan.union.controller;

import com.nkhan.union.model.ArticleDto;
import com.nkhan.union.model.ProductDto;
import com.nkhan.union.model.SearchResult;
import com.nkhan.union.model.UserDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class SearchController {

    @QueryMapping
    public Flux<SearchResult> search(@Argument String keyword) {
        return Flux.just(
                UserDto.builder()
                        .id("u1")
                        .username("nasruddin")
                        .email("nasr@gmail.com")
                        .build(),
                ProductDto.builder()
                        .id("p1")
                        .name("iPhone 15")
                        .price(999.0)
                        .build(),
                ArticleDto.builder()
                        .id("a1")
                        .title("GraphQL in Spring Boot")
                        .content("This is a tutorial...")
                        .build()
        );
    }
}