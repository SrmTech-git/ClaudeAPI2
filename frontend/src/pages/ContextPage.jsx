import React, { useState, useEffect } from 'react';
import { getContext, updateContext } from '../api';

/**
 * Context page component for managing system prompts.
 * Allows users to set custom context that will be included in all Claude API calls.
 */
function ContextPage() {
  const [systemPrompt, setSystemPrompt] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState(null);

  // Load context on mount
  useEffect(() => {
    loadContext();
  }, []);

  /**
   * Load the current context from the backend.
   */
  const loadContext = async () => {
    try {
      setLoading(true);
      const data = await getContext();
      setSystemPrompt(data.systemPrompt || '');
    } catch (error) {
      console.error('Error loading context:', error);
      setMessage({ type: 'error', text: 'Failed to load context' });
    } finally {
      setLoading(false);
    }
  };

  /**
   * Save the context to the backend.
   */
  const handleSave = async () => {
    try {
      setSaving(true);
      setMessage(null);
      await updateContext(systemPrompt);
      setMessage({ type: 'success', text: 'Context saved successfully!' });

      // Clear success message after 3 seconds
      setTimeout(() => {
        setMessage(null);
      }, 3000);
    } catch (error) {
      console.error('Error saving context:', error);
      setMessage({ type: 'error', text: 'Failed to save context' });
    } finally {
      setSaving(false);
    }
  };

  /**
   * Clear the context.
   */
  const handleClear = () => {
    setSystemPrompt('');
  };

  if (loading) {
    return (
      <div className="context-container">
        <div className="loading">Loading context...</div>
      </div>
    );
  }

  return (
    <div className="context-container">
      <div className="context-header">
        <h1>Context / System Prompt</h1>
        <p className="context-description">
          This text will be included as a system prompt in all conversations with Claude.
          Use it to set custom instructions, context, or behavior guidelines.
        </p>
      </div>

      {message && (
        <div className={`message-banner ${message.type}`}>
          {message.text}
        </div>
      )}

      <div className="context-editor">
        <textarea
          value={systemPrompt}
          onChange={(e) => setSystemPrompt(e.target.value)}
          placeholder="Enter your custom system prompt here...&#10;&#10;Example:&#10;You are a helpful assistant who always responds in a friendly and professional manner."
          className="context-textarea"
          disabled={saving}
        />

        <div className="context-actions">
          <button
            onClick={handleSave}
            disabled={saving}
            className="btn-primary"
          >
            {saving ? 'Saving...' : 'Save Context'}
          </button>

          <button
            onClick={handleClear}
            disabled={saving}
            className="btn-secondary"
          >
            Clear
          </button>
        </div>
      </div>

      <div className="context-info">
        <h3>How it works:</h3>
        <ul>
          <li>The system prompt is sent with every message to Claude</li>
          <li>It helps guide Claude's behavior and responses</li>
          <li>Changes apply to all new conversations immediately</li>
          <li>Leave empty to use Claude's default behavior</li>
        </ul>
      </div>
    </div>
  );
}

export default ContextPage;
