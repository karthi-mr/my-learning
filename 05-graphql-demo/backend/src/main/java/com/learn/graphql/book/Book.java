package com.learn.graphql.book;

public record Book(
        Integer id,

        String name,

        Integer pageCount,

        Integer authorId
) {
}
