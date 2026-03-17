import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";

function ChatApp() {
  const [isConnected, setIsConnected] = useState(false);
  const [sender, setSender] = useState("");
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);

  const stompClientRef = useRef(null);

  function connect() {
    const client = new Client({
      brokerURL: "ws://localhost:8080/ws",
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
      onConnect: () => {
        setIsConnected(true);

        client.subscribe("/topic/messages", (msg) => {
          const body = JSON.parse(msg.body);
          console.log("body:", body);
          setMessages(prevMessages => [...prevMessages, body]);
        });
        client.subscribe("/topic/messages1", (msg) => {
          const body = JSON.parse(msg.body);
          console.log("secret body:", body);
          // setMessages(prevMessages => [...prevMessages, body]);
        });
      },
      onStompError: (frame) => {
        console.log("Broker error:", frame.headers["message"]);
        console.log("Details:", frame.body);
      },
      onWebSocketClose: () => {
        setIsConnected(false);
      }
    });
    client.activate();
    stompClientRef.current = client;
  }

  function disconnect() {
    stompClientRef.current?.deactivate();
    setIsConnected(false);
  }

  function sendMessage() {
    if (!message.trim() || !sender.trim()) return;

    stompClientRef.current.publish({
      destination: "/app/chat",
      body: JSON.stringify({
        sender,
        content: message
      })
    });
    setMessage("");
  }

  function sendSecretMessage() {
    if (!message.trim() || !sender.trim()) return;

    stompClientRef.current.publish({
      destination: "/app/chat1",
      body: JSON.stringify({
        sender,
        content: message
      })
    });
    setMessage("");
  }

  useEffect(() => {
    return () => {
      stompClientRef.current?.deactivate();
    }
  }, []);

  return(
    <div style={{ padding: "20px" }}>
      <h2>Simple Chat</h2>

      <input
        placeholder="Your name"
        value={sender}
        onChange={(e) => setSender(e.target.value)}
      />

      {!isConnected ? (
        <button onClick={connect}>Connect</button>
      ) : (
        <button onClick={disconnect}>Disconnect</button>
      )}

      <div style={{ marginTop: "20px" }}>
        <input
          placeholder="Type message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        /> &nbsp;
        <button onClick={sendMessage} disabled={!isConnected}>
          Send
        </button> &nbsp;
        <button onClick={sendSecretMessage} disabled={!isConnected}>
          Send Secret
        </button>
      </div>

      <ul>
        {messages.map((msg, index) => (
          <li key={index}>
            <strong>{msg.sender}:</strong> {msg.content}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ChatApp;
