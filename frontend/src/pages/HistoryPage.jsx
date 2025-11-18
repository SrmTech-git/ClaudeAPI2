import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllConversations, deleteConversation, getConversation } from '../api';

/**
 * Format timestamp to readable string.
 */
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  return date.toLocaleString();
}

/**
 * Export a conversation to a text file.
 */
async function exportConversation(conversationId) {
  try {
    // Fetch the full conversation
    const conversation = await getConversation(conversationId);

    // Build the text content
    let content = `Claude Chat Conversation Export\n`;
    content += `================================\n\n`;
    content += `Conversation ID: ${conversation.id}\n`;
    content += `Created: ${formatTimestamp(conversation.createdAt)}\n`;
    content += `Last Updated: ${formatTimestamp(conversation.updatedAt)}\n\n`;
    content += `Total Tokens:\n`;
    content += `  Input: ${conversation.totalInputTokens}\n`;
    content += `  Output: ${conversation.totalOutputTokens}\n`;
    content += `  Cache Creation: ${conversation.totalCacheCreationTokens}\n`;
    content += `  Cache Read: ${conversation.totalCacheReadTokens}\n`;
    content += `\n================================\n\n`;

    // Add messages
    conversation.messages.forEach((msg, index) => {
      const messageNumber = index + 1;
      const role = msg.role === 'user' ? 'You' : 'Claude';

      content += `[${messageNumber}] ${role}\n`;
      content += `Timestamp: ${formatTimestamp(msg.createdAt)}\n`;
      content += `\n${msg.content}\n`;

      // Add thinking block if present
      if (msg.thinking) {
        content += `\n--- Extended Thinking ---\n`;
        content += `${msg.thinking}\n`;
        content += `--- End Thinking ---\n`;
      }

      // Add token usage for assistant messages
      if (msg.role === 'assistant' && msg.inputTokens) {
        content += `\nTokens: ${msg.inputTokens} in, ${msg.outputTokens} out`;
        if (msg.cacheCreationTokens > 0) {
          content += ` | Cache created: ${msg.cacheCreationTokens}`;
        }
        if (msg.cacheReadTokens > 0) {
          content += ` | Cache read: ${msg.cacheReadTokens}`;
        }
        content += `\n`;
      }

      content += `\n================================\n\n`;
    });

    // Create and download the file
    const blob = new Blob([content], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `claude-conversation-${conversationId}-${Date.now()}.txt`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  } catch (error) {
    console.error('Error exporting conversation:', error);
    alert('Failed to export conversation.');
  }
}

/**
 * History page showing all past conversations.
 */
function HistoryPage() {
  const [conversations, setConversations] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Load conversations on mount
  useEffect(() => {
    loadConversations();
  }, []);

  /**
   * Load all conversations from backend.
   */
  const loadConversations = async () => {
    try {
      setLoading(true);
      const data = await getAllConversations();
      setConversations(data);
    } catch (error) {
      console.error('Error loading conversations:', error);
      alert('Failed to load conversations.');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Delete a conversation.
   */
  const handleDelete = async (conversationId) => {
    if (!confirm('Are you sure you want to delete this conversation?')) {
      return;
    }

    try {
      await deleteConversation(conversationId);
      // Remove from local state
      setConversations((prev) =>
        prev.filter((conv) => conv.id !== conversationId)
      );
    } catch (error) {
      console.error('Error deleting conversation:', error);
      alert('Failed to delete conversation.');
    }
  };

  /**
   * Continue a conversation (navigate to chat page).
   * Note: This would require updating ChatPage to accept a conversation ID.
   * For now, we'll just show an alert.
   */
  const handleContinue = (conversationId) => {
    // TODO: Implement continuing a conversation in ChatPage
    alert(`Continue conversation feature coming soon! Conversation ID: ${conversationId}`);
    // In a full implementation, you would:
    // navigate('/', { state: { conversationId } });
  };

  /**
   * Export a conversation.
   */
  const handleExport = async (conversationId) => {
    await exportConversation(conversationId);
  };

  if (loading) {
    return <div className="loading">Loading conversations...</div>;
  }

  return (
    <div className="history-container">
      <div className="history-header">
        <h1>Conversation History</h1>
      </div>

      {conversations.length === 0 ? (
        <div className="empty-state">
          <h2>No conversations yet</h2>
          <p>Start chatting to create your first conversation!</p>
        </div>
      ) : (
        <div className="conversation-list">
          {conversations.map((conv) => (
            <div key={conv.id} className="conversation-card">
              <div className="conversation-header">
                <div className="conversation-info">
                  <div className="conversation-date">
                    Started: {formatTimestamp(conv.createdAt)}
                  </div>
                  <div className="conversation-date">
                    Last updated: {formatTimestamp(conv.updatedAt)}
                  </div>
                  <div className="conversation-stats">
                    Messages: {conv.messages.length} | Total tokens: {conv.totalInputTokens + conv.totalOutputTokens}
                  </div>
                </div>

                <div className="conversation-actions">
                  <button
                    onClick={() => handleContinue(conv.id)}
                    className="btn-small"
                  >
                    Continue
                  </button>
                  <button
                    onClick={() => handleExport(conv.id)}
                    className="btn-small btn-secondary"
                  >
                    Export
                  </button>
                  <button
                    onClick={() => handleDelete(conv.id)}
                    className="btn-small btn-danger"
                  >
                    Delete
                  </button>
                </div>
              </div>

              {/* Preview first few messages */}
              <div className="conversation-preview">
                {conv.messages.slice(0, 4).map((msg, index) => (
                  <div key={index} className="preview-message">
                    <div className="preview-role">
                      {msg.role === 'user' ? 'You' : 'Claude'}:
                    </div>
                    <div className="preview-content">
                      {msg.content.substring(0, 100)}
                      {msg.content.length > 100 ? '...' : ''}
                    </div>
                  </div>
                ))}
                {conv.messages.length > 4 && (
                  <div className="preview-message">
                    <div className="preview-content" style={{ fontStyle: 'italic' }}>
                      ... and {conv.messages.length - 4} more messages
                    </div>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default HistoryPage;
