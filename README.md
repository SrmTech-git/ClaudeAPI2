# Claude Chat Interface - Monorepo

A full-stack application for chatting with Claude Sonnet 4.5, featuring extended thinking display, conversation history, and token usage tracking.

## Project Structure

```
ClaudeAPI2/
├── frontend/          # React + Vite frontend
├── backend/           # Java + Spring Boot backend
└── README.md          # This file
```

## Features

- **Real-time Chat**: Interactive chat interface with Claude Sonnet 4.5
- **Extended Thinking**: Displays Claude's thinking process during responses
- **5-Minute Cache Timer**: Visual countdown to optimize prompt caching
- **Conversation History**: Browse, continue, or delete past conversations
- **Token Tracking**: Monitor input, output, and cache tokens per message
- **Export Conversations**: Download chat history as formatted text files
- **PostgreSQL Storage**: Persistent storage of all conversations and messages

## Quick Start

### Prerequisites

- Node.js 18+ and npm
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- PGAdmin (optional, for database management)

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Create `src/main/resources/application-secrets.properties`:
   ```properties
   anthropic.api.key=YOUR_API_KEY_HERE
   ```

3. Set up PostgreSQL database:
   ```sql
   CREATE DATABASE claude_chat;
   ```

4. Run the backend:
   ```bash
   mvn spring-boot:run
   ```

Backend runs on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run the development server:
   ```bash
   npm run dev
   ```

Frontend runs on `http://localhost:5173`

## Technology Stack

### Frontend
- React 18
- Vite
- React Router
- Axios for API calls

### Backend
- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Maven

## Security

- API keys are stored in `application-secrets.properties` (gitignored)
- No authentication required (single-user application)

## License

MIT
