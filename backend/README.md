# Claude Chat Backend

Spring Boot backend service for the Claude chat interface.

## Features

- REST API for chat operations
- PostgreSQL database for conversation persistence
- Claude API integration with extended thinking
- Token usage tracking
- CORS enabled for frontend connection

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 14+

## Database Setup

1. Install PostgreSQL and create a database:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE claude_chat;

# Exit
\q
```

2. Update database credentials in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/claude_chat
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## API Key Configuration

1. Copy the example secrets file:

```bash
cp src/main/resources/application-secrets.properties.example src/main/resources/application-secrets.properties
```

2. Edit `src/main/resources/application-secrets.properties` and add your Anthropic API key:

```properties
anthropic.api.key=sk-ant-your-actual-api-key-here
```

**IMPORTANT**: Never commit the `application-secrets.properties` file. It is already in `.gitignore`.

## Running the Application

```bash
# Install dependencies and run
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`.

## API Endpoints

### Send a Chat Message

```http
POST /api/chat
Content-Type: application/json

{
  "conversationId": null,  // or ID to continue conversation
  "message": "Hello Claude!"
}
```

Response:
```json
{
  "conversationId": 1,
  "messageId": 2,
  "content": "Hello! How can I help you today?",
  "thinking": "The user is greeting me...",
  "tokenUsage": {
    "inputTokens": 10,
    "outputTokens": 20,
    "cacheCreationTokens": 0,
    "cacheReadTokens": 0
  }
}
```

### Get All Conversations

```http
GET /api/conversations
```

### Get Specific Conversation

```http
GET /api/conversations/{id}
```

### Delete Conversation

```http
DELETE /api/conversations/{id}
```

## Project Structure

```
backend/
├── src/main/java/com/claudechat/
│   ├── ClaudeChatApplication.java    # Main application
│   ├── controller/
│   │   └── ChatController.java       # REST endpoints
│   ├── service/
│   │   ├── ChatService.java          # Business logic
│   │   └── ClaudeApiClient.java      # Claude API integration
│   ├── model/
│   │   ├── Conversation.java         # Database entity
│   │   └── Message.java              # Database entity
│   ├── repository/
│   │   ├── ConversationRepository.java
│   │   └── MessageRepository.java
│   └── dto/
│       ├── ChatRequest.java
│       ├── ChatResponse.java
│       ├── ConversationDTO.java
│       └── MessageDTO.java
└── src/main/resources/
    ├── application.properties
    └── application-secrets.properties  # Your API key (gitignored)
```

## Database Schema

The application automatically creates these tables:

### conversations
- id (primary key)
- created_at
- updated_at
- total_input_tokens
- total_output_tokens
- total_cache_creation_tokens
- total_cache_read_tokens

### messages
- id (primary key)
- conversation_id (foreign key)
- role (user/assistant)
- content
- thinking (nullable)
- created_at
- input_tokens
- output_tokens
- cache_creation_tokens
- cache_read_tokens

## Development

The application uses:
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Database access
- **PostgreSQL** - Database
- **WebFlux** - HTTP client for Claude API
- **Jackson** - JSON processing
