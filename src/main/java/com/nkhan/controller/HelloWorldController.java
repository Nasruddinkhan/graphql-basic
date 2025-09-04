package com.nkhan.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Controller
public class HelloWorldController {

    @QueryMapping
    public Mono<String> sayHello(){
        return Mono.just("Hello Graphql first program");
    }

    @QueryMapping("hello")
    public Mono<String> helloName(){
        return Mono.just("Query mapping with alias");
    }

    @QueryMapping
    public Mono<String> sayHelloTo(@Argument("name") String userName){
        return Mono.fromSupplier(()-> """
                Hello %s, Welcome to Graphql world
                """.formatted(userName));
    }

    @QueryMapping
    public Mono<Integer> randomId(){
        return Mono.fromSupplier(()-> ThreadLocalRandom.current().nextInt(1, 1000));
    }


}
