# Claude Chat Frontend

React frontend for the Claude chat interface built with Vite.

## Features

- **Chat Interface**: Clean, intuitive chat UI with message bubbles
- **Extended Thinking Display**: Shows Claude's thinking process
- **5-Minute Cache Timer**: Visual countdown for optimal prompt caching
- **Conversation History**: Browse all past conversations
- **Export Conversations**: Download chat history as text files
- **Token Tracking**: Display token usage for each message
- **Delete Conversations**: Remove unwanted conversation history

## Prerequisites

- Node.js 18 or higher
- npm

## Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Running the Application

Start the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`.

## Building for Production

Build the application:

```bash
npm run build
```

Preview the production build:

```bash
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── pages/
│   │   ├── ChatPage.jsx          # Main chat interface with timer
│   │   └── HistoryPage.jsx       # Conversation history page
│   ├── api.js                    # Backend API client
│   ├── App.jsx                   # Main app with routing
│   ├── main.jsx                  # App entry point
│   └── index.css                 # Global styles
├── index.html                    # HTML template
├── vite.config.js                # Vite configuration
└── package.json                  # Dependencies
```

## Features Explained

### Chat Interface

- Type messages in the textarea
- Press Enter to send (Shift+Enter for new line)
- Messages display in conversation bubbles
- Thinking blocks shown in highlighted yellow boxes
- Token usage displayed below each Claude response

### 5-Minute Timer

- Counts down from 5:00 after each response
- Turns red when under 1 minute
- Helps optimize prompt caching (cache expires after 5 minutes)
- Resets automatically when Claude responds

### History Page

- View all past conversations
- See message previews
- Continue conversations (coming soon)
- Export conversations as text files
- Delete conversations

### Export Format

Exported conversations include:
- Conversation metadata (ID, timestamps)
- Total token usage
- All messages numbered sequentially
- Thinking blocks (if present)
- Individual message token counts
- Timestamps for each message

## Technology Stack

- **React 18** - UI framework
- **Vite** - Build tool
- **React Router** - Navigation
- **Axios** - HTTP client

## Configuration

The frontend is configured to connect to the backend at `http://localhost:8080/api`.

To change this, update the `API_BASE_URL` in `src/api.js`:

```javascript
const API_BASE_URL = 'http://your-backend-url/api';
```

## Troubleshooting

**Cannot connect to backend:**
- Ensure the backend is running on port 8080
- Check CORS configuration in backend
- Verify the API_BASE_URL in `src/api.js`

**Timer not working:**
- Check browser console for errors
- Ensure React is rendering correctly

**Export not downloading:**
- Check browser's download settings
- Ensure pop-up blocker isn't blocking downloads
