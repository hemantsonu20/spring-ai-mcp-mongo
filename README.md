# spring-ai-mcp-mongo

A full-stack AI-powered query system using Spring Boot, MongoDB, and AI Language Models (Ollama or OpenAI). The system consists of two main components:

- **mcp-client**: REST API client for user queries. Supports both Ollama (local) and OpenAI (cloud) providers for LLM responses, and communicates with mcp-server for structured data queries.
- **mcp-server**: Provides tools (APIs) to find details related to people. Queries person data from MongoDB.
- **AI Providers**:
  - **Ollama** (default): Local LLM server for natural language processing. Free and runs locally.
  - **OpenAI**: Cloud-based AI service with GPT models. Requires API key.
- **MongoDB**: Stores person data in a `person` collection.


### Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant Client as mcp-client
    participant LLM as AI Provider<br/>(Ollama/OpenAI)
    participant Server as mcp-server
    participant DB as MongoDB

    User->>Client: (1) Sends query for Person Data
    Client->>Server: (2) Fetch all MCP Tools
    Server-->>Client: (3) List of MCP Tools as JSON
    Client->>LLM: (4) Request with User Query + MCP Tools
    LLM-->>Client: (5) Response with Tool calls (if needed)
    Client->>Server: (6) Request to Execute Tool (if needed)
    Server->>DB: (7) Query person collection
    DB-->>Server: (8) Person Data Result
    Server-->>Client: (9) Tool Execution Result as JSON
    Client->>LLM: (10) Final prompt with Tool Execution Result
    LLM-->>Client: (11) NLP Based Response
    Client-->>User: (12) Final answer as Text
```

#### Step-by-step Explanation

1. **User sends query for Person Data:**
   - The user initiates a request (e.g., "Find all people older than age 44") to the mcp-client API.
2. **Client fetches all MCP Tools:**
   - mcp-client requests the list of available tools (APIs) from mcp-server that can be used to answer the query.
3. **Server returns list of MCP Tools as JSON:**
   - mcp-server responds with a JSON list describing available tools and their capabilities.
4. **Client sends request with User Query + MCP Tools to LLM:**
   - mcp-client sends the user query and the list of tools to the selected AI provider (Ollama or OpenAI) for reasoning and planning.
5. **LLM responds with Tool calls (if needed):**
   - The AI provider analyzes the query and tools, and may respond with instructions to call a specific tool (API) on mcp-server.
6. **Client requests Server to Execute Tool (if needed):**
   - mcp-client sends a request to mcp-server to execute the required tool (e.g., findPeopleOlderThan(age=44)).
7. **Server queries person collection in DB:**
   - mcp-server queries the MongoDB person collection to fetch the relevant data.
8. **DB returns Person Data Result:**
   - MongoDB returns the query result to mcp-server.
9. **Server returns Tool Execution Result as JSON:**
   - mcp-server sends the tool execution result back to mcp-client.
10. **Client sends final prompt with Tool Execution Result to LLM:**
    - mcp-client sends the tool result and original query to the AI provider for final natural language response generation.
11. **LLM returns NLP Based Response:**
    - The AI provider generates a user-friendly, natural language answer based on the data and returns it to mcp-client.
12. **Client returns final answer as Text to User:**
    - mcp-client sends the final answer back to the user.

## MongoDB Schema (Person)
MongoDB has a single collection named `person` that stores details about persons.
Each document in the `person` collection has the following structure:

```json
{
  "_id": "ObjectId",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "age": "int",
  "street": "string",
  "city": "string",
  "state": "string",
  "zipCode": "string"
}
```

## AI Provider Configuration

The mcp-client supports two AI providers:

### Ollama (Default)
- **Local, Free**: Runs on your machine
- **Model**: llama3.2 (configured for tool calling support)
- **Setup**: Automatically started via Docker Compose
- **No API key required**

### OpenAI
- **Cloud-based**: Requires internet connection
- **Model**: gpt-4o-mini (configured)
- **Setup**: Requires API key
- **Set environment variable**:
  ```bash
  export OPENAI_API_KEY=your-api-key-here
  ```

### Selecting Provider at Runtime

By default, all queries use **Ollama**. To use OpenAI, add `"provider": "openai"` to your request:

**Using Ollama (default):**
```json
{
  "query": "Find all people older than age 44"
}
```

**Using OpenAI:**
```json
{
  "query": "Find all people older than age 44",
  "provider": "openai"
}
```

### Supported Ollama Models for Tool Calling

The following Ollama models support function/tool calling:
- ✅ **llama3.2** (3B, 1B) - Default, good balance
- ✅ **llama3.1** (8B, 70B) - Excellent function calling
- ✅ **qwen2.5** (7B+) - Very good support
- ✅ **mistral-nemo** (12B) - Good support
- ❌ **mistral:7b** - Limited/no support

To use a different model, update `mcp-client/src/main/resources/application.yml`:
```yaml
spring:
  ai:
    ollama:
      chat:
        options:
          model: llama3.1  # Change model here
```

## Development

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven

### Build Project

```sh
./mvnw clean install
```

### Run Project
- Run MongoDB and Ollama using Docker Compose.
```sh
docker compose up -d
```
> Ollama downloads models on first run, so it may take some time. Next time it will be faster as it uses volumes to cache models.

- Run mcp-server app <br/>
  <small>Run mcp-server via IDE or command line.</small>
```sh
./mvnw spring-boot:run -pl mcp-server
```
- Run mcp-client  app  <br/>
  <small>Run mcp-client via IDE or command line.</small>
```sh
./mvnw spring-boot:run -pl mcp-client
```
  > **Note**: To use OpenAI provider, set the `OPENAI_API_KEY` environment variable before running mcp-client:
  > ```sh
  > export OPENAI_API_KEY=your-api-key-here
  > ./mvnw spring-boot:run -pl mcp-client
  > ```

### Build and Run (All Services in Docker)
```sh
./mvnw clean install && docker compose --profile client-server up -d --build
```
> This builds the project and starts MongoDB, Ollama, mcp-client, and mcp-server in Docker containers. It also exposes ports 8080 (mcp-server) and 8082 (mcp-client) so that you can access them via localhost.

## Execute mcp-server Tools (Optional)
You can use the Model Context Protocol Inspector to manually execute mcp-server tools.
```sh
npx @modelcontextprotocol/inspector java -jar  mcp-server/target/mcp-server-1.0.0.jar 
```

## Examples

### Query with Ollama (Default)
Send a query using the default Ollama provider:
```sh
curl --location 'http://localhost:8082/query' \
--header 'Content-Type: application/json; charset=UTF-8' \
--header 'Accept: */*' \
--data '{
    "query": "Find all people older than age 44"
}'
```

### Query with OpenAI
Send a query using the OpenAI provider:
```sh
curl --location 'http://localhost:8082/query' \
--header 'Content-Type: application/json; charset=UTF-8' \
--header 'Accept: */*' \
--data '{
    "query": "Find all people older than age 44",
    "provider": "openai"
}'
```

**Sample Response:**
```json
{
    "response": "Here are the people who are older than 44 years old that I found:\n\n* Andrew U Lewis, age 45, living at 999 Hickory Ln, Dallas, TX, zip code 75202.\n* Henry K Ward, age 45, living at 5100 Linden Ave, Fort Worth, TX, zip code 76106."
}
```

### More Example Queries

**Find people by city:**
```json
{
    "query": "Who lives in Dallas?"
}
```

**Find people by state:**
```json
{
    "query": "Show me all people from Texas"
}
```

**Find people in age range:**
```json
{
    "query": "Find people between 30 and 40 years old"
}
```

**Find by last name:**
```json
{
    "query": "Find all people with last name Smith"
}
```

## License
[Apache License 2.0](LICENSE)
