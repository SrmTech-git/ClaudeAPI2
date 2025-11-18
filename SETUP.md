# Quick Setup Guide

Follow these steps to get the Claude Chat Interface running locally.

## Prerequisites

Install these before starting:

- **Java 17+**: [Download here](https://adoptium.net/)
- **Maven 3.8+**: [Download here](https://maven.apache.org/download.cgi)
- **Node.js 18+**: [Download here](https://nodejs.org/)
- **PostgreSQL 14+**: [Download here](https://www.postgresql.org/download/)
- **PGAdmin** (optional): [Download here](https://www.pgadmin.org/)

## Step-by-Step Setup

### 1. Database Setup

```bash
# Start PostgreSQL (method varies by OS)
# On macOS with Homebrew:
brew services start postgresql

# On Linux:
sudo systemctl start postgresql

# Create the database
psql -U postgres
```

In the PostgreSQL prompt:
```sql
CREATE DATABASE claude_chat;
\q
```

### 2. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Copy the secrets template
cp src/main/resources/application-secrets.properties.example \
   src/main/resources/application-secrets.properties

# Edit the secrets file and add your Anthropic API key
# Use your favorite text editor:
nano src/main/resources/application-secrets.properties
```

In `application-secrets.properties`, add:
```properties
anthropic.api.key=sk-ant-YOUR_ACTUAL_API_KEY_HERE
```

**Important**: Get your API key from [Anthropic Console](https://console.anthropic.com/)

If your PostgreSQL credentials are different from the defaults, edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Start the backend:
```bash
mvn spring-boot:run
```

You should see:
```
Started ClaudeChatApplication in X.XXX seconds
```

The backend is now running on `http://localhost:8080`

### 3. Frontend Setup

Open a new terminal window:

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

You should see:
```
VITE v5.x.x  ready in XXX ms

➜  Local:   http://localhost:5173/
```

### 4. Access the Application

Open your browser and go to:
```
http://localhost:5173
```

You should see the chat interface!

## Verify Everything Works

1. **Test the chat**:
   - Type "Hello!" in the chat box
   - Press Enter
   - You should see Claude respond

2. **Check the timer**:
   - After Claude responds, you should see "Cache expires in: 5:00"
   - It should count down

3. **Check the history**:
   - Click "History" in the navigation
   - You should see your conversation listed
   - Try exporting it

4. **Check the database**:
   ```bash
   psql -U postgres -d claude_chat
   ```
   ```sql
   SELECT * FROM conversations;
   SELECT * FROM messages;
   \q
   ```

## Common Issues

### Backend Issues

**"Cannot connect to database"**
```bash
# Make sure PostgreSQL is running
psql -U postgres -c "SELECT 1"

# Check if database exists
psql -U postgres -l | grep claude_chat
```

**"API key not found"**
- Make sure `application-secrets.properties` exists
- Check that it's in `src/main/resources/`
- Verify the API key is correct

**Port 8080 already in use**
- Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```
- Update frontend's `src/api.js` to match

### Frontend Issues

**"Cannot connect to backend"**
- Make sure backend is running on port 8080
- Check `src/api.js` has correct URL
- Check browser console for CORS errors

**npm install fails**
- Try deleting `node_modules` and `package-lock.json`
- Run `npm install` again
- Check Node.js version: `node --version`

### Database Issues

**"Database does not exist"**
```bash
psql -U postgres -c "CREATE DATABASE claude_chat;"
```

**Wrong credentials**
- Update `application.properties` with your PostgreSQL username/password

## Default Credentials

### PostgreSQL
- Host: `localhost`
- Port: `5432`
- Database: `claude_chat`
- Username: `postgres`
- Password: `postgres` (or your PostgreSQL password)

### Application
- Backend: `http://localhost:8080`
- Frontend: `http://localhost:5173`

## Next Steps

Once everything is running:

1. **Get your Anthropic API key** from https://console.anthropic.com/
2. **Add it to the secrets file** as shown in Step 2
3. **Restart the backend** if you added the key after starting
4. **Start chatting!**

## Stopping the Application

To stop:

1. **Frontend**: Press `Ctrl+C` in the terminal running Vite
2. **Backend**: Press `Ctrl+C` in the terminal running Maven
3. **PostgreSQL** (optional):
   ```bash
   # macOS
   brew services stop postgresql

   # Linux
   sudo systemctl stop postgresql
   ```

## Development Tips

- Backend auto-reloads when you change Java files (thanks to DevTools)
- Frontend auto-reloads when you change React files (thanks to Vite)
- Check backend logs for API errors
- Check browser console for frontend errors
- Use PGAdmin to browse the database visually

## Need Help?

- Check `CLAUDE.md` for detailed project documentation
- Check `backend/README.md` for backend-specific info
- Check `frontend/README.md` for frontend-specific info
- Check `README.md` for project overview
