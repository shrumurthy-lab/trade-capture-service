# Instructions Capture Service

Trade Instruction Capture Service

This project is a Spring Boot application designed to ingest financial trade instruction data from multiple sources
(REST API via file upload and Kafka inbound topic), process and transform the data according to business rules,
store a canonical audit record in an in-memory store, and publish the final,
sanitized trade to an outbound Kafka topic for downstream systems.

- Accepts trade instructions via CSV file upload
- Accepts inbound Kafka messages (`instructions.inbound`)
- Normalizes and transforms data to canonical format
- Produces platform-specific JSON
- Publishes transformed messages to Kafka (`instructions.outbound`)
- Stores canonical copy in in-memory storage

The solution uses Docker Compose to manage the application, Kafka, and Zookeeper services for a complete, isolated
environment.

*******Getting Started*******

Prerequisites

Docker & Docker Compose: Required to build and run the application and infrastructure services (Kafka/Zookeeper).

Java 17+ & Maven: Required if you need to build the JAR manually or run unit tests on the host.

1. Build the Project

Ensure you are in the project root directory and build the executable JAR:

mvn clean package -DskipTests

2. Start the Environment

Use Docker Compose to build the application image, set up the network, and start all services in detached mode:

docker compose up --build -d

3. Check Status

Verify that all three services are running:

docker compose ps

All services (app, kafka, zookeeper) should show up and running:

*******Testing and Verification*******

The application exposes two endpoints. All curl and docker compose exec commands should be run from host machine's
terminal.

1. Ingestion Method A: REST API File Upload

This tests the file processing logic and internal publishing mechanism.

a. Upload CSV File (Sample file is in sample-input folder )

Uploads the sample trades.csv which contains 2 trades.

curl -X POST "http://localhost:8080/api/trades/upload" \
-H "accept: application/json" \
-H "Content-Type: multipart/form-data" \
-F "file=@trades.csv;type=text/csv"

2. Ingestion Method B: Kafka Listener

This tests the KafkaListenerService

a. Run the Kafka Producer

This opens an interactive producer session inside the Kafka container.

docker compose exec kafka kafka-console-producer --topic instructions.inbound --bootstrap-server kafka:29092

b. Inject a Sample Trade

When the > prompt appears, paste the JSON message and hit Enter. Press Ctrl+C to exit the producer.

{"accountNumber":"000054321000","securityId":"AAA000","tradeType":"Buy","amount":1000,"timestamp":"2025-11-22T23:59:
59Z"}

*******Verification Endpoints*******

After ingestion, use these commands to confirm processing.

1. Verify In-Memory Canonical Store

Retrieves all raw, canonical trade records currently held in the ConcurrentHashMap.

curl -X GET "http://localhost:8080/api/trades/store" \
-H "accept: application/json"

2. Verify Kafka Output Topic (instructions.outbound)

This confirms that the transformation logic (masking, normalization) and publishing step were successful.

docker compose exec kafka kafka-console-consumer --topic instructions.outbound --bootstrap-server kafka:29092
--from-beginning

You should see all processed trades (from files and the producer) as transformed PlatformTrade JSON messages.

*******API Documentation  (Swagger/OpenAPI)

The project includes built-in, interactive API documentation using Springdoc OpenAPI. This documentation provides a
complete overview of the REST endpoints, expected input formats, and response schemas.

Accessing the Documentation

With the application running, the documentation is accessible at two endpoints:

Swagger UI (Interactive Interface):
Open your browser and navigate to:
http://localhost:8080/swagger-ui.html

Here you can explore the endpoints, view request/response models, and use the "Try it out" feature to execute API calls
directly.

This endpoint is used for integration with external tools like Postman, code generators, or API gateways.

Documented Endpoints

POST /api/trades/upload: Handles file uploads for trade instructions (CSV or JSON).

GET /api/trades/store: Retrieves the current state of the in-memory canonical trade store.

*******Cleanup*******

To stop and remove all containers, volumes, and network components created by Docker Compose:

docker compose down -v

This ensures a clean slate for the next run.