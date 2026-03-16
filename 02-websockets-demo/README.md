# WebSockets Demo with Spring Boot and React

A simple full-stack project to learn how WebSockets work using:

- **Spring Boot** for the backend
- **React + Vite** for the frontend
- **Native WebSocket API** for real-time communication

This project demonstrates basic WebSocket communication without using a database, SockJS, or STOMP. It is focused on understanding the core WebSocket flow in a clean and beginner-friendly way.

---

## Project Overview

This demo contains two WebSocket examples:

1. **Simple WebSocket Endpoint**
    - Endpoint: `/ws`
    - Used for basic client-server communication
    - The server receives a message and sends a direct response back to the same client

2. **Chat WebSocket Endpoint**
    - Endpoint: `/chat`
    - Used for a simple chat-style interaction
    - When one client sends a message, the server broadcasts it to other connected clients
    - The server also notifies others when a user joins or disconnects

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring WebSocket
- Jackson

### Frontend
- React
- Vite
- JavaScript
- Native Browser WebSocket API

---
## How It Works
### Simple WebSocket Handler
- The SimpleWebSocketHandler is used for one-to-one communication.
- A client connects to /ws 
- The server logs the connection
- The client sends a text message
- The server responds with:
```
Server received: <your-message>
```

This is useful for understanding the WebSocket lifecycle:
- connection opened
- message received
- response sent
- connection closed
---

### Chat WebSocket Handler

The ChatWebSocketHandler is used for a simple multi-client chat demo.

- A client connects to /chat
- The session is stored in an in-memory set
- When a user sends a message:
  - the message is wrapped into a DTO 
  - the server broadcasts it to other connected clients

- When a user connects or disconnects:
  - other connected users receive a notification

This helps demonstrate:
- maintaining active sessions
- broadcasting messages
- handling join/leave events
- sending structured data as JSON
---

### Features
- Real-time communication using WebSockets
- Basic direct response example
- Basic chat-style broadcasting example
- Join and disconnect notifications
- React UI to send and display messages
- Beginner-friendly project structure
---

## Learning Goals
This project is useful for understanding:
- what WebSockets are
- how WebSocket communication differs from normal HTTP
- how to configure WebSockets in Spring Boot
- how to build a custom TextWebSocketHandler
- how to manage connected client sessions
- how to send direct and broadcast messages
- how to connect a React frontend to a WebSocket server

***If you found this project helpful, consider giving the repository a star.***