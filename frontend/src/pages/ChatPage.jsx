import React, { useState, useEffect, useRef } from 'react';
import { sendMessage } from '../api';

/**
 * Format timestamp to readable string.
 */
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  return date.toLocaleString();
}

/**
 * Format time remaining in MM:SS format.
 */
function formatTimeRemaining(seconds) {
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins}:${secs.toString().padStart(2, '0')}`;
}

/**
 * Chat page component with 5-minute cache timer.
 */
function ChatPage() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [conversationId, setConversationId] = useState(null);
  const [timeRemaining, setTimeRemaining] = useState(300); // 5 minutes in seconds
  const messagesEndRef = useRef(null);
  const timerRef = useRef(null);

  // Scroll to bottom when new messages arrive
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  // Timer countdown
  useEffect(() => {
    // Clear any existing timer
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }

    // Start new timer
    timerRef.current = setInterval(() => {
      setTimeRemaining((prev) => {
        if (prev <= 0) {
          clearInterval(timerRef.current);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    // Cleanup on unmount
    return () => {
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }
    };
  }, [conversationId]); // Reset timer when conversation changes

  /**
   * Reset the 5-minute timer.
   */
  const resetTimer = () => {
    setTimeRemaining(300);
  };

  /**
   * Handle sending a message.
   */
  const handleSend = async () => {
    if (!input.trim() || loading) return;

    const userMessage = input.trim();
    setInput('');
    setLoading(true);

    // Add user message to UI
    const userMsg = {
      role: 'user',
      content: userMessage,
      createdAt: new Date().toISOString(),
    };
    setMessages((prev) => [...prev, userMsg]);

    try {
      // Send to backend
      const response = await sendMessage(userMessage, conversationId);

      // Update conversation ID if this is a new conversation
      if (!conversationId) {
        setConversationId(response.conversationId);
      }

      // Add assistant message to UI
      const assistantMsg = {
        role: 'assistant',
        content: response.content,
        thinking: response.thinking,
        createdAt: new Date().toISOString(),
        tokenUsage: response.tokenUsage,
      };
      setMessages((prev) => [...prev, assistantMsg]);

      // Reset the timer after receiving response
      resetTimer();
    } catch (error) {
      console.error('Error sending message:', error);
      alert('Failed to send message. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle Enter key to send (Shift+Enter for new line).
   */
  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  /**
   * Start a new conversation.
   */
  const handleNewChat = () => {
    setMessages([]);
    setConversationId(null);
    setInput('');
    resetTimer();
  };

  return (
    <div className="chat-container">
      {/* Header with New Chat button */}
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '1rem' }}>
        <h1>Chat with Claude</h1>
        <button onClick={handleNewChat} className="btn-secondary">
          New Chat
        </button>
      </div>

      {/* Messages area */}
      <div className="messages">
        {messages.length === 0 ? (
          <div className="empty-state">
            <h2>Start a conversation</h2>
            <p>Ask Claude anything!</p>
          </div>
        ) : (
          messages.map((msg, index) => (
            <div key={index} className={`message ${msg.role}`}>
              <div className="message-role">
                {msg.role === 'user' ? 'You' : 'Claude'}
              </div>
              <div className="message-content">{msg.content}</div>

              {/* Show thinking block if present */}
              {msg.thinking && (
                <div className="message-thinking">
                  <div className="thinking-label">Extended Thinking:</div>
                  <div className="message-content">{msg.thinking}</div>
                </div>
              )}

              <div className="message-timestamp">
                {formatTimestamp(msg.createdAt)}
              </div>

              {/* Show token usage for assistant messages */}
              {msg.tokenUsage && (
                <div className="message-tokens">
                  Tokens: {msg.tokenUsage.inputTokens} in, {msg.tokenUsage.outputTokens} out
                  {msg.tokenUsage.cacheCreationTokens > 0 &&
                    ` | Cache created: ${msg.tokenUsage.cacheCreationTokens}`}
                  {msg.tokenUsage.cacheReadTokens > 0 &&
                    ` | Cache read: ${msg.tokenUsage.cacheReadTokens}`}
                </div>
              )}
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input area with timer */}
      <div className="input-container">
        <div className="input-wrapper">
          {/* Cache timer - shows warning when under 1 minute */}
          <div className={`timer ${timeRemaining < 60 ? 'warning' : ''}`}>
            Cache expires in: {formatTimeRemaining(timeRemaining)}
          </div>

          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Type your message... (Press Enter to send, Shift+Enter for new line)"
            disabled={loading}
          />
        </div>

        <button onClick={handleSend} disabled={loading || !input.trim()}>
          {loading ? 'Sending...' : 'Send'}
        </button>
      </div>
    </div>
  );
}

export default ChatPage;
