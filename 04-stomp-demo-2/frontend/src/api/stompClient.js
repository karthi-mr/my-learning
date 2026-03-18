import { Client } from "@stomp/stompjs";

let client = null;

export function connectWebSocket({
  username,
  onPublicMessage,
  onPrivateMessage,
  onTyping,
  onPresence,
  onConnected,
  onDisconnected
}) {
  client = new Client({
    brokerURL: "ws://localhost:8080/ws",
    reconnectDelay: 5000,
    debug: (payload) => { console.log("payload:", payload); },
    connectHeaders: {
      username: username
    }
  });

  client.onConnect = () => {
    client.subscribe("/topic/public", (message) => {
      console.log("public:", JSON.parse(message.body));
      onPublicMessage(JSON.parse(message.body));
    });
    client.subscribe("/topic/typing", (message) => {
      onTyping(JSON.parse(message.body));
    });
    client.subscribe("/topic/presence", (message) => {
      onPresence(JSON.parse(message.body));
    });
    client.subscribe("/user/queue/private", (message) => {
      console.log("private message:", JSON.parse(message.body))
      onPrivateMessage(JSON.parse(message.body));
    });

    client.publish({
      destination: "/app/chat.addUser",
      body: JSON.stringify({
        sender: username
      })
    });
    onConnected?.();
  };

  client.onWebSocketClose = () => {
    onDisconnected?.();
  }

  client.activate();
}

export function sendPublicMessage(sender, content) {
  if (!client?.connected) return;

  client.publish({
    destination: "/app/chat.send",
    body: JSON.stringify({
      sender,
      content
    })
  });
}

export function sendPrivateMessage(sender, recipient, content) {
  if (!client?.connected) return;

  console.log(`sending private message from: ${sender}, to: ${recipient}, with content: ${content}.`);

  client.publish({
    destination: "/app/chat.private",
    body: JSON.stringify({
      sender,
      recipient,
      content
    })
  });
}

export function sendTyping(sender, recipient = null) {
  if (!client?.connected) return;

  client.publish({
    destination: "/app/chat.typing",
    body: JSON.stringify({
      sender,
      recipient
    })
  });
}

export function disconnectWebSocket() {
  client?.deactivate();
}
