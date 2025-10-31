package com.nkhan.cache.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class OperationalCacheController {

    @QueryMapping("getDataFromOperationalCache")
    public Mono<String> getOperation(@Argument("name") String name){
        log.info("name = {}", name);
        return Mono.fromSupplier(() -> """
                Hello, %s Now it's working""".formatted(name));
    }
}
