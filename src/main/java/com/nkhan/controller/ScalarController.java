package com.nkhan.controller;

import com.nkhan.customer.model.AllScalarType;
import com.nkhan.customer.model.Role;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
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
}
