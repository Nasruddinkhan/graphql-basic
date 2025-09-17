package com.nkhan.config;

import com.nkhan.payment.model.WalletDto;
import com.nkhan.union.model.ArticleDto;
import com.nkhan.union.model.ProductDto;
import com.nkhan.union.model.UserDto;
import graphql.scalars.ExtendedScalars;
import graphql.schema.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.ClassNameTypeResolver;
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
                .scalar(ExtendedScalars.DateTime)
                .type("PaymentMethod", typeWiring -> typeWiring.typeResolver(typeResolver()))
                .type("SearchResult", typeWiring -> typeWiring.typeResolver(typeResolver()));


    }

    @Bean
    public TypeResolver typeResolver(){
        ClassNameTypeResolver resolver = new ClassNameTypeResolver();
        resolver.addMapping(WalletDto.class, "Wallet");
        resolver.addMapping(ArticleDto.class, "Article");
        resolver.addMapping(ProductDto.class, "Products");
        resolver.addMapping(UserDto.class, "User");

        return resolver;
    }
}
