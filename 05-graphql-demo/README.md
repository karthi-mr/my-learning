# GraphQL Demo with Spring Boot

A simple GraphQL demo project built with Spring Boot to understand the fundamentals of GraphQL, including query handling, argument-based fetching, and nested field resolution. This project uses in-memory data for books and authors, so it is easy to run and learn without any database setup.

## Features

- Built with Spring Boot and Spring for GraphQL
- Uses Java 21
- Supports fetching all books
- Supports fetching a book by id
- Supports fetching all authors
- Supports fetching an author by id
- Demonstrates nested data fetching for book author details
- Includes GraphiQL UI for testing GraphQL queries in the browser
- Uses in-memory sample data, so no database configuration is needed

## Tech Stack

- Java 21
- Spring Boot 4.0.5
- Spring for GraphQL
- Spring Web MVC
- Lombok
- Maven

## What This Project Demonstrates

This project is useful for learning how GraphQL works in a Spring Boot application. It shows how to expose GraphQL queries using `@QueryMapping`, how to accept query arguments using `@Argument`, and how to resolve nested fields using `@SchemaMapping`. In this project, the author details for each book are resolved separately based on the `authorId` stored in the book record.

## Sample Data

The application contains in-memory sample records for books and authors.

### Authors
- John Doe
- Kevin Peterson
- Kiran Anwar

### Books
- Atomic Habits
- 5AM Club
- Java: The complete guide
- Spring Boot masters
- React complete course

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java 21
- Maven

### Run the Application

```bash
cd backend
mvn spring-boot:run
```

Or if you want to use Maven Wrapper:

```bash
cd backend
./mvnw spring-boot:run
```

For Windows:

```bash
cd backend
mvnw.cmd spring-boot:run
```

The application starts from the BackendApplication class.

## Access GraphiQL

After starting the application, open the GraphiQL UI in your browser:

http://localhost:8080/graphiql

GraphiQL is enabled in the application configuration with the /graphiql path.

---

## Example Queries
### Get all books
```graphql
query {
  books {
    id
    name
    pageCount
  }
}
```

### Get a book by id
```graphql
query {
  bookById(id: 1) {
    id
    name
    pageCount
  }
}
```

### Get all authors
```graphql
query {
  authors {
    id
    name
  }
}
```

### Get an author by id
```graphql
query {
  authorById(id: 2) {
    id
    name
  }
}
```

### Get books with author details
```graphql
query {
  books {
    id
    name
    pageCount
    author {
      id
      name
    }
  }
}
```

These queries are supported by the query mapping methods in the controllers and the nested author resolution in the book controller.

---
## Key Learning Points
- Understanding the basics of GraphQL in Spring Boot
- Writing GraphQL query handlers using @QueryMapping
- Passing arguments with @Argument
- Resolving nested fields with @SchemaMapping
- Testing GraphQL APIs using GraphiQL
- Building a simple API without a database for learning purposes

## Author
Developed as a learning project to explore GraphQL with Spring Boot.
