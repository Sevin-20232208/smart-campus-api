# Smart Campus REST API

**Author:** W.G. Sevin Kawsika  
**Student IIT ID:** 20232208  
**Student UOW ID:** w2120210  

## 1. Overview of API Design
The Smart Campus API is a RESTful web service built using Java and the JAX-RS (Jersey) framework. It is designed to manage environmental sensors deployed across different rooms in a university campus. 

**Key Design Features:**
* **Resource URIs:** Follows standard REST conventions using clear, pluralized nouns (e.g., `/api/v1/rooms`, `/api/v1/sensors`, `/api/v1/readings`).
* **Data Format:** Accepts and produces JSON data formats exclusively.
* **In-Memory Storage:** Utilizes a mock, thread-safe database (ConcurrentHashMap) for temporary data storage during the demonstration.
* **Error Handling:** Features custom Exception Mappers to gracefully handle errors (e.g., 404 Not Found, 409 Conflict, 422 Unprocessable Entity) and return standardized JSON error messages instead of server stack traces.
* **Logging:** Implements a ContainerRequestFilter and ContainerResponseFilter to log all incoming requests and outgoing responses.

## 2. Build and Launch Instructions
This project was built using Apache NetBeans and is designed to run on an Apache Tomcat Server.

**Prerequisites:**
* Java Development Kit (JDK 8 or higher)
* Apache NetBeans IDE
* Apache Tomcat Server (v9.0 or higher)

**Steps to run the server:**
1. Clone or download this repository to your local machine.
2. Open **Apache NetBeans**.
3. Go to `File` > `Open Project` and select the `SmartCampusAPI` folder.
4. In the Projects window, right-click the project name and select **Clean and Build** to resolve all dependencies.
5. Right-click the project again and select **Run**.
6. NetBeans will automatically deploy the application to your configured Apache Tomcat server.
7. The base URL for the API will be: `http://localhost:8080/SmartCampusAPI/api/v1`

## 3. Sample cURL Commands
Below are five sample cURL commands that demonstrate successful interactions with the API. 

*Note: These commands are pre-formatted specifically for the **Windows Command Prompt (cmd)**. The JSON payloads use escaped double-quotes (`\"`) to ensure they run smoothly without syntax errors.*

**Get all rooms (GET)**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms
```
**Create a new room (POST)**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\": \"ENG-101\", \"name\": \"Engineering Lab\", \"capacity\": 60}"
```
**Get sensors filtered by type (GET)**
```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```
**Create a new sensor linked to the room (POST)**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\": \"TEMP-005\", \"type\": \"Temperature\", \"status\": \"ACTIVE\", \"currentValue\": 0.0, \"roomId\": \"LAB-101\"}"
```
**Add a reading to the sensor (POST)**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/readings -H "Content-Type: application/json" -d "{\"id\": \"READ-500\", \"sensorId\": \"TEMP-005\", \"value\": 24.2}"
```

## 📝 API Design & Implementation Report

### Part 1: Lifecycle & HATEOAS

**Q1: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures.**

**Answer:** Per-default, JAX-RS resource classes run on a per-request basis. The runtime creates a brand-new resource instance for every incoming request. Because these instances are temporary, it is unsafe to store campus data directly into the resource instance's field variables, as they are not intended to hold shared state. Instead, campus data is stored within a central repository (a mock database). To prevent race conditions or inconsistent updates from concurrent users, writes to these repositories are made thread-safe using concurrent Java collections.

**Q2: Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?**

**Answer:** Hypermedia is a high level of REST functionality because it provides links for actions and resources available based on the returned data. This allows for better discovery of options. For client developers, this is superior to static documentation because it reduces coupling; if the server changes a URI pattern, the client can still function by following the links provided dynamically by the server.

### Part 2: Collection Management & Idempotency

**Q1: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.**

**Answer:** Returning only IDs reduces the payload size (saving bandwidth) and is useful for clients that only need references. However, the drawback is that the client must perform additional requests to get room details, increasing network round-trips and client-side complexity. Conversely, sending full objects uses more bandwidth but allows the client to display all information in a single request. To meet coursework requirements and provide a complete experience, this API returns full room objects.

**Q2: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.**

**Answer:** Yes, the DELETE method is idempotent. If a client sends a DELETE request, the room is removed. If the client sends the exact same request again, the room is already gone, so no further change occurs to the server state. While the second response might be a `404 Not Found` instead of a `204 No Content`, the fact that the server state does not change after the first successful deletion makes the operation idempotent.

### Part 3: Content Negotiation & Filtering

**Q1: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format. How does JAX-RS handle this mismatch?**

**Answer:** The `@Consumes` annotation restricts the method to JSON payloads. If a client sends data in another format (like XML or text), JAX-RS cannot associate the request with the method. Consequently, JAX-RS will reject the request before it reaches the logic and return an **HTTP 415 Unsupported Media Type** status code.

**Q2: Contrast filtering using @QueryParam with an alternative design where the type is part of the URL path. Why is the query parameter approach generally considered superior?**

**Answer:** Using `@QueryParam` is superior because it indicates that filtering is an optional operation on a resource collection. A path parameter defines a specific resource, whereas a query parameter allows for multiple optional filters (e.g., `type=CO2&status=ACTIVE`) without creating a rigid and complex URL structure.

### Part 4: Architectural Patterns

**Q1: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity?**

**Answer:** The Sub-Resource Locator pattern allows us to separate logic into different classes (e.g., separating Sensor logic from Room logic). This manages complexity by avoiding a "massive controller" or "God Class." By delegating nested paths to separate classes, the code becomes more modular, maintainable, and easier to debug as the API scales.

### Part 5: Exception Mapping & Logging

**Q1: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**

**Answer:** HTTP 422 (Unprocessable Entity) indicates that the request was formatted correctly, but the content contains a validation problem (like a non-existent Room ID reference). HTTP 404 implies the URI itself was not found. Therefore, 422 is a more exact description of a logic/validation error within a correctly received payload.

**Q2: Explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather?**

**Answer:** Stack traces expose sensitive internal details such as class names, file paths, and system structures. An attacker could use this information to identify software versions or vulnerabilities to plan a security breach. Using structured JSON error messages hides these details and keeps the system secure.

**Q3: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging?**

**Answer:** JAX-RS Filters centralize logging in one place. This ensures all requests and responses are logged consistently, eliminates the need to duplicate `Logger` statements in every resource method, and separates cross-cutting concerns from the actual business logic.
