package com.nkhan.union.model;

import lombok.Builder;

@Builder
public record  ArticleDto(  String id,
                            String title,
                            String content) implements SearchResult{
}
