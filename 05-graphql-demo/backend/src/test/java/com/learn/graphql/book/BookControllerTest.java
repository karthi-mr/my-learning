package com.learn.graphql.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest
@AutoConfigureGraphQlTester
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    private BookService bookService;

    @Test
    void canGetBooks() {
        this.graphQlTester
                .documentName("books")
                .execute()
                .path("books")
                .entityList(Book.class)
                .hasSize(5);
    }
}