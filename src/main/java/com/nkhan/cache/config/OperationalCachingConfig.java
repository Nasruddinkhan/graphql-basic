package com.nkhan.cache.config;

import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class OperationalCachingConfig {

    @Bean
    public GraphQlSourceBuilderCustomizer sourceBuilderCustomizer(PreparsedDocumentProvider provider){
        return builder -> builder.configureGraphQl(configurer ->
                configurer.preparsedDocumentProvider(provider));
    }
  /*
    @Bean
    public PreparsedDocumentProvider provider(){
        return new PreparsedDocumentProvider() {
            Map<String, PreparsedDocumentEntry> map = new HashMap<>();
            @Override
            public CompletableFuture<PreparsedDocumentEntry> getDocumentAsync(ExecutionInput executionInput, Function<ExecutionInput, PreparsedDocumentEntry> parseAndValidateFunction) {
              var documentEntry =  map.computeIfAbsent(executionInput.getQuery(), key->{
                   log.info("Not Found = {}", key);
                  var  document = parseAndValidateFunction.apply(executionInput);
                  log.info("caching = {}", document);
                  return document;
                });
                return CompletableFuture.completedFuture(documentEntry);
            }
        };
    }*/
  @Bean
  public PreparsedDocumentProvider preparsedDocumentProvider() {
      Map<String, PreparsedDocumentEntry> cache = new ConcurrentHashMap<>();
      return (executionInput, parseAndValidateFunction) ->
              CompletableFuture.supplyAsync(() ->
                      cache.computeIfAbsent(executionInput.getQuery(), query -> {
                          log.debug("Cache miss for GraphQL query. Parsing and validating...");
                          PreparsedDocumentEntry entry = parseAndValidateFunction.apply(executionInput);
                          log.debug("Query cached successfully: {}", query);
                          return entry;
                      })
              );
  }
}
