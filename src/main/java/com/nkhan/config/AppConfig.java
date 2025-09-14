package com.nkhan.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class AppConfig {

    @Bean
    public RuntimeWiringConfigurer configurer(){
        return  c -> c.scalar(ExtendedScalars.GraphQLLong)
                .scalar(ExtendedScalars.GraphQLByte)
                .scalar(ExtendedScalars.GraphQLShort)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.GraphQLBigInteger)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.LocalTime)
                .scalar(ExtendedScalars.Object)
                .scalar(ExtendedScalars.DateTime);

    }
}
