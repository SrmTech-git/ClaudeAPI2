import axios from 'axios';

// Base URL for the backend API
const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Send a message to Claude.
 * @param {string} message - The user's message
 * @param {number|null} conversationId - Optional conversation ID to continue
 * @returns {Promise} - Response containing Claude's reply and token usage
 */
export const sendMessage = async (message, conversationId = null) => {
  const response = await api.post('/chat', {
    message,
    conversationId,
  });
  return response.data;
};

/**
 * Get all conversations.
 * @returns {Promise} - List of all conversations
 */
export const getAllConversations = async () => {
  const response = await api.get('/conversations');
  return response.data;
};

/**
 * Get a specific conversation by ID.
 * @param {number} conversationId - The conversation ID
 * @returns {Promise} - The conversation with all messages
 */
export const getConversation = async (conversationId) => {
  const response = await api.get(`/conversations/${conversationId}`);
  return response.data;
};

/**
 * Delete a conversation.
 * @param {number} conversationId - The conversation ID
 * @returns {Promise}
 */
export const deleteConversation = async (conversationId) => {
  await api.delete(`/conversations/${conversationId}`);
};

export default api;
