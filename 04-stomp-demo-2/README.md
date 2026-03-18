# 📡 STOMP Demo 2 – Real-Time Messaging with WebSockets

A simple demo project showcasing **real-time communication using STOMP over WebSockets**. This project demonstrates how clients can send and receive messages through a broker in a publish-subscribe model.

---

## 🚀 Features

- 🔄 Real-time messaging using WebSockets
- 📬 STOMP protocol for structured messaging
- 👥 Publish/Subscribe (Pub/Sub) model
- 🌐 Simple client UI for testing messaging
- ⚡ Lightweight and easy to run

---

## 🧱 Tech Stack

- **Backend:** Spring Boot (Java)
- **Messaging Protocol:** STOMP
- **Transport:** WebSockets
- **Frontend:** React

---

## ⚙️ How It Works

1. Client connects to the WebSocket endpoint
2. STOMP protocol establishes messaging communication
3. Client sends messages to a specific destination
4. Server broadcasts messages to subscribed clients

---

## 🔌 WebSocket Endpoints

| Endpoint | Description |
|----------|------------|
| `/ws`    | WebSocket connection endpoint |
| `/app`   | Application destination prefix |
| `/topic` | Broadcast messages to subscribers |

---

## ▶️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/karthi-mr/my-learning.git
cd my-learning/04-stomp-demo-2
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

---

## 🌐 Access the Application

```
http://localhost:8080
```

---

## 🧪 Testing the Demo

1. Open multiple browser tabs/windows
2. Connect to the WebSocket server
3. Send messages from one client
4. Observe real-time updates across all clients

---

## 💡 Example Flow

- User A sends a message
- Server receives it via `/app/...`
- Server broadcasts to `/topic/...`
- All subscribed users receive the message instantly

---

## 📌 Key Concepts

- **WebSocket:** Full-duplex communication channel
- **STOMP:** Messaging protocol over WebSocket
- **Broker:** Handles message routing
- **Topics:** Channels for broadcasting messages

---

## 🤝 Contributing

Feel free to fork this repo and submit pull requests!

---

## 📜 License

This project is for learning purposes.

---

***If you like this project, please give it a ⭐ — your support keeps me motivated to build more!***
