# Claude Chat Interface - Project Overview

This document provides a comprehensive overview of the Claude Chat Interface monorepo for Claude AI assistants working on this project.

## Project Purpose

A full-stack chat application that allows users to interact with Claude Sonnet 4.5 with the following key features:
- Real-time chat with extended thinking display
- 5-minute cache timer for optimal token usage
- Conversation history with export capabilities
- Token tracking per message and conversation
- PostgreSQL persistence

## Architecture

### Monorepo Structure
```
ClaudeAPI2/
├── backend/           # Java Spring Boot API
├── frontend/          # React + Vite UI
├── README.md          # User documentation
├── CLAUDE.md          # This file
└── SETUP.md           # Quick setup guide
```

### Backend (Java + Spring Boot + PostgreSQL)

**Technology Stack:**
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL database
- Maven build tool
- WebFlux for Claude API calls

**Key Components:**

1. **Entities** (`model/`):
   - `Conversation`: Stores conversation metadata and total token usage
   - `Message`: Stores individual messages with thinking blocks and token counts

2. **Repositories** (`repository/`):
   - `ConversationRepository`: JPA repository for conversations
   - `MessageRepository`: JPA repository for messages

3. **Services** (`service/`):
   - `ClaudeApiClient`: Handles API calls to Anthropic with extended thinking enabled
   - `ChatService`: Orchestrates conversation logic, message storage, and API calls

4. **Controllers** (`controller/`):
   - `ChatController`: REST endpoints for chat and history operations

5. **DTOs** (`dto/`):
   - `ChatRequest`: User message input
   - `ChatResponse`: Claude's response with tokens
   - `ConversationDTO`: Full conversation data
   - `MessageDTO`: Individual message data

**API Endpoints:**
- `POST /api/chat` - Send a message
- `GET /api/conversations` - Get all conversations
- `GET /api/conversations/{id}` - Get specific conversation
- `DELETE /api/conversations/{id}` - Delete conversation

**Configuration:**
- `application.properties`: Main config (database, server, CORS)
- `application-secrets.properties`: API key (GITIGNORED)

**Security:**
- API key stored in gitignored file
- CORS configured for localhost:5173
- No authentication (single-user app)

### Frontend (React + Vite)

**Technology Stack:**
- React 18
- Vite (build tool)
- React Router (navigation)
- Axios (HTTP client)

**Key Components:**

1. **Pages:**
   - `ChatPage.jsx`: Main chat interface with 5-minute timer
   - `HistoryPage.jsx`: Conversation history with export/delete

2. **API Client:**
   - `api.js`: Axios-based client for backend communication

3. **Routing:**
   - `App.jsx`: Main app with React Router setup
   - Navigation between Chat and History pages

**Key Features:**

1. **Chat Interface:**
   - Message bubbles for user and Claude
   - Extended thinking display in yellow boxes
   - Token usage display per message
   - Auto-scroll to new messages
   - Enter to send, Shift+Enter for new line

2. **5-Minute Timer:**
   - Counts down from 5:00
   - Resets after each Claude response
   - Turns red under 1 minute
   - Helps users stay within cache window

3. **History Page:**
   - List all conversations
   - Preview first 4 messages
   - Export to text file with full formatting
   - Delete conversations
   - Continue conversations (TODO)

4. **Export Format:**
   - Conversation metadata
   - Numbered messages (1, 2, 3...)
   - Timestamps
   - Thinking blocks
   - Token usage

## Database Schema

### conversations table
```sql
- id: BIGINT (PK)
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
- total_input_tokens: INTEGER
- total_output_tokens: INTEGER
- total_cache_creation_tokens: INTEGER
- total_cache_read_tokens: INTEGER
```

### messages table
```sql
- id: BIGINT (PK)
- conversation_id: BIGINT (FK)
- role: VARCHAR (user/assistant)
- content: TEXT
- thinking: TEXT (nullable)
- created_at: TIMESTAMP
- input_tokens: INTEGER
- output_tokens: INTEGER
- cache_creation_tokens: INTEGER
- cache_read_tokens: INTEGER
```

## Claude API Integration

The `ClaudeApiClient` sends requests to the Anthropic API with:
- Model: `claude-sonnet-4-5-20250929`
- Max tokens: 4096
- Extended thinking enabled with 10k token budget
- Conversation history for context

Response parsing extracts:
- Text content blocks
- Thinking blocks
- Token usage (input, output, cache creation, cache read)

## Development Workflow

### Running Locally

1. **Start PostgreSQL**:
   ```bash
   # Ensure PostgreSQL is running
   psql -U postgres -c "CREATE DATABASE claude_chat;"
   ```

2. **Configure Backend**:
   ```bash
   cd backend
   cp src/main/resources/application-secrets.properties.example \
      src/main/resources/application-secrets.properties
   # Edit and add API key
   ```

3. **Start Backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   # Runs on http://localhost:8080
   ```

4. **Start Frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   # Runs on http://localhost:5173
   ```

### Key Files to Know

**Never commit:**
- `backend/src/main/resources/application-secrets.properties`
- `backend/target/`
- `frontend/node_modules/`
- `frontend/dist/`

**Important for configuration:**
- `backend/src/main/resources/application.properties` - DB and API config
- `backend/pom.xml` - Java dependencies
- `frontend/package.json` - Node dependencies
- `frontend/src/api.js` - Backend URL configuration

## Common Tasks

### Adding a New API Endpoint

1. Add method to `ChatController`
2. Implement logic in `ChatService`
3. Add API call to `frontend/src/api.js`
4. Use in React components

### Modifying Database Schema

1. Update entity classes (`model/Conversation.java` or `model/Message.java`)
2. Spring Boot auto-updates schema on restart (ddl-auto=update)
3. For production, use proper migrations

### Adding New Features

1. Backend: Create service methods, add endpoints
2. Frontend: Create/update components, add API calls
3. Test locally before committing

## Token Usage Tracking

Tokens are tracked at two levels:

1. **Per Message**: Stored in `messages` table
   - Input tokens (prompt)
   - Output tokens (response)
   - Cache creation tokens
   - Cache read tokens

2. **Per Conversation**: Aggregated in `conversations` table
   - Total of all message tokens
   - Updated when new messages are added

## Future Enhancements

- Continue conversations from history page
- User authentication
- Multiple users
- Conversation search
- Message editing
- Regenerate responses
- Custom system prompts
- Model selection

## Troubleshooting

**Backend won't start:**
- Check PostgreSQL is running
- Verify database exists
- Check API key in secrets file
- Check port 8080 is available

**Frontend can't connect:**
- Ensure backend is running
- Check CORS configuration
- Verify API_BASE_URL in api.js

**Database errors:**
- Check PostgreSQL connection
- Verify credentials in application.properties
- Check if database exists

**Claude API errors:**
- Verify API key is correct
- Check API quota/limits
- Ensure proper request format
