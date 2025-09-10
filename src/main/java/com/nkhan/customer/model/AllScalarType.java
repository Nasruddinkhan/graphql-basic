package com.nkhan.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Builder
public record AllScalarType(
        String id,
        Integer age,
        Float amount,
        String name,
        Boolean isValid,
        Double distance,
        Byte currentYear,
        Short currentMonth,
        BigDecimal bigDecimal,
        BigInteger bigInteger,
        LocalDate date,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime time,
        LocalDateTime dateTime,
        Role role
) {}


