package com.nkhan.controller;

import com.nkhan.controller.model.Product;
import com.nkhan.customer.model.AllScalarType;
import com.nkhan.customer.model.Role;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ScalarController {

    @QueryMapping("get")
    public Mono<AllScalarType>  getAllTypes(){
        System.out.println("its called");
        final var  isTrue =   ThreadLocalRandom.current().nextBoolean();
        return Mono.just(AllScalarType.builder()
                .id("12345")
                .age(30)
                .amount(2500.75f)
                .name("Nasruddin khan")
                .isValid(true)
                .distance(123.45)
                .currentYear((byte) 25)
                .currentMonth((short) 9)
                .bigDecimal(BigDecimal.valueOf(9999.99))
                .bigInteger(BigInteger.valueOf(1234567890L))
                .date(LocalDate.now())
                .time(LocalTime.now())
                .dateTime(OffsetDateTime.now())
                .role(isTrue ? Role.ADMIN : Role.USER) // assuming you have an enum Role { ADMIN, USER, ... }
                .build());
    }
    @QueryMapping("products")
    public Flux<Product> getProducts() {
        return Flux.just(
                Product.builder()
                        .name("Laptop")
                        .attribute(Map.of(
                                "brand", "Dell",
                                "price", 1200,
                                "inStock", true
                        ))
                        .build(),

                Product.builder()
                        .name("Phone")
                        .attribute(Map.of(
                                "brand", "Samsung",
                                "price", 800,
                                "color", "Black"
                        ))
                        .build(),

                Product.builder()
                        .name("Headphones")
                        .attribute(Map.of(
                                "brand", "Sony",
                                "wireless", true,
                                "price", 150
                        ))
                        .build()
        );
    }


}
