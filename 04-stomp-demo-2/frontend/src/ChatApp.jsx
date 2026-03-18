import { useEffect, useMemo, useRef, useState } from "react";
import {
  connectWebSocket,
  disconnectWebSocket,
  sendPrivateMessage,
  sendPublicMessage,
  sendTyping
} from "./api/stompClient.js";

function ChatApp() {
  const [username, setUsername] = useState("");
  const [joined, setJoined] = useState(false);
  const [connected, setConnected] = useState(false);

  const [publicMessages, setPublicMessages] = useState([]);
  const [privateMessages, setPrivateMessages] = useState([]);
  const [onlineUsers, setOnlineUsers] = useState([]);
  const [typingText, setTypingText] = useState("");

  const [message, setMessage] = useState("");
  const [privateMessage, setPrivateMessage] = useState("");
  const [selectedUser, setSelectedUser] = useState("");

  const typingTimeoutRef = useRef(null);
  const typingThrottleRef = useRef(0);

  const visibleOnlineUsers = useMemo(
    () => onlineUsers.filter(user => user !== username),
    [onlineUsers, username]);

  useEffect(() => {
    return () => disconnectWebSocket();
  }, []);

  function handleJoin() {
    const trimmed = username.trim();
    if (!trimmed) return;

    connectWebSocket({
      username: trimmed,
      onPublicMessage: (msg) => {
        setPublicMessages(prev => [...prev, msg]);
      },
      onPrivateMessage: (msg) => {
        setPrivateMessages(prev => [...prev, msg]);
      },
      onTyping: (msg) => {
        if (msg.sender === trimmed) return;
        if (msg.recipient && msg.recipient !== trimmed) return;

        setTypingText(
          msg.recipient
            ? `${msg.sender} is typing to you...`
            : `${msg.sender} is typing`
        );

        clearTimeout(typingTimeoutRef.current);
        typingTimeoutRef.current = setTimeout(() => {
          setTypingText("");
        }, 1500);
      },
      onPresence: (payload) => {
        setOnlineUsers(Array.isArray(payload.onlineUsers) ? payload.onlineUsers : []);
      },
      onConnected: () => {
        setConnected(true);
        setJoined(true);
      },
      onDisconnected: () => {
        setConnected(false);
      }
    })
  }

  function handleSendPublic() {
    const trimmed = message.trim();
    if (!trimmed) return;
    sendPublicMessage(username, trimmed);
    setMessage("");
  }

  function handleSendPrivate() {
    const trimmed = privateMessage.trim();
    if (!trimmed || !selectedUser) return;
    sendPrivateMessage(username, selectedUser, trimmed);
    setPrivateMessage("");
  }

  function handleTyping(recipient = null) {
    const now = Date.now();
    if (now - typingThrottleRef.current < 1000) return;
    typingThrottleRef.current = now;
    sendTyping(username, recipient);
  }

  console.log("private:", privateMessages)
  const filteredPrivateMessages = privateMessages.filter(
    msg =>
      (msg.sender === username && msg.recipient === selectedUser) ||
      (msg.sender === selectedUser && msg.recipient === username)
  );

  if (!joined) {
    return (
      <div className="join-container">
        <div className="card">
          <h1>Chat App</h1>
          <p>Enter your username to join</p>
          <input
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
            onKeyDown={(e) => e.key === "Enter" && handleJoin()}
          />
          <button onClick={handleJoin}>Join Chat</button>
        </div>
      </div>
    );
  }

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="card">
          <h2>{username}</h2>
          <p className={connected ? "status connected" : "status disconnected"}>
            {connected ? "Connected" : "Disconnected"}
          </p>
        </div>

        <div className="card">
          <h3>Online Users</h3>
          {visibleOnlineUsers.length === 0 ? (
            <p>No other users online</p>
          ) : (
            <ul className="user-list">
              {visibleOnlineUsers.map((user) => (
                <li
                  key={user}
                  className={selectedUser === user ? "active-user" : ""}
                  onClick={() => setSelectedUser(user)}
                >
                  {user}
                </li>
              ))}
            </ul>
          )}
        </div>
      </aside>

      <main className="main">
        <section className="card chat-section">
          <h2>Public Chat</h2>
          <div className="messages">
            {publicMessages.map((msg, index) => (
              <div key={index} className={`message ${msg.sender === username ? "own" : ""}`}>
                <div className="meta">
                  <strong>{msg.sender}</strong>
                  <span>{msg.type}</span>
                </div>
                <div>{msg.content}</div>
              </div>
            ))}
          </div>

          <div className="input-row">
            <input
              value={message}
              onChange={(e) => {
                setMessage(e.target.value);
                handleTyping();
              }}
              placeholder="Type public message"
              onKeyDown={(e) => e.key === "Enter" && handleSendPublic()}
            />
            <button onClick={handleSendPublic}>Send</button>
          </div>
        </section>

        <section className="card chat-section">
          <h2>Private Chat {selectedUser ? `- ${selectedUser}` : ""}</h2>
          {!selectedUser ? (
            <p>Select a user from the online list</p>
          ) : (
            <>
              <div className="messages">
                {filteredPrivateMessages.map((msg, index) => (
                  <div key={index} className={`message ${msg.sender === username ? "own" : ""}`}>
                    <div className="meta">
                      <strong>{msg.sender}</strong>
                      <span>PRIVATE</span>
                    </div>
                    <div>{msg.content}</div>
                  </div>
                ))}
              </div>

              <div className="input-row">
                <input
                  value={privateMessage}
                  onChange={(e) => {
                    setPrivateMessage(e.target.value);
                    handleTyping(selectedUser);
                  }}
                  placeholder={`Message ${selectedUser}`}
                  onKeyDown={(e) => e.key === "Enter" && handleSendPrivate()}
                />
                <button onClick={handleSendPrivate}>Send</button>
              </div>
            </>
          )}

          {typingText && <p className="typing">{typingText}</p>}
        </section>
      </main>
    </div>
  );
}

export default ChatApp;
