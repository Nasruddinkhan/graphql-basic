package com.nkhan.controller.model;

import lombok.Builder;

import java.util.Map;

@Builder
public record Product(String name, Map<String, Object> attribute) {

}
