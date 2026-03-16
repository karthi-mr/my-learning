import { useEffect, useRef, useState } from "react";

function App() {
  const [socketStatus, setSocketStatus] = useState("Disconnected");
  const [inputMessage, setInputMessage] = useState("");
  const [messages, setMessages] = useState([]);
  const socketRef = useRef(null);

  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/ws");
    socketRef.current = socket;

    socket.onopen = () => {
      setSocketStatus("Connected");
      console.log("Connected to server");
    }

    socket.onmessage = (event) => {
      setMessages(prev => [...prev, event.data]);
    }

    socket.onclose = () => {
      setSocketStatus("Disconnected");
      console.log("Disconnected from server");
    }

    return () => {
      socket.close();
    }
  }, []);

  function sendMessage() {
    if (!inputMessage.trim()) return;

    if (socketRef.current && socketRef.current.readyState === WebSocket.OPEN) {
      socketRef.current.send(inputMessage);
      setMessages(prev => [...prev, "You: " + inputMessage]);
      setInputMessage("");
    }
  }

  return (
    <div style={{ padding: "30px", fontFamily: "Arial" }}>
      <h1>Simple WebSocket Demo</h1>
      <p>Status: {socketStatus}</p>

      <input
        type="text"
        value={inputMessage}
        onChange={(e) => setInputMessage(e.target.value)}
        placeholder="Type a message"
        style={{ padding: "8px", width: "250px", marginRight: "10px" }}
      />

      <button onClick={sendMessage} style={{ padding: "8px 12px" }}>
        Send
      </button>

      <h3>Messages</h3>
      <ul>
        {messages.map((msg, index) => (
          <li key={index}>{msg}</li>
        ))}
      </ul>
    </div>
  );
}

export default App;
