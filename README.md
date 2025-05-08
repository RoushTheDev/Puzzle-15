# 15 Puzzle Game
by Michael Due Pedersen

## Requirements
- Java 21

## Setup
1. Run the Spring Boot application
2. Navigate to `http://localhost:9000/swagger-ui/index.html#/`
3. Log in (see `CustomUserDetailsService.java` for credentials)
4. Start playing

## Future Development
- Introduce RabbitMQ to terminate games
- Refactor termination to keep the game as terminated instead of removing it
- Scheduled cleanup of inactive games
- View all games as an admin
- Rework security with encoded passwords and token expiration and refreshing
- Add rate limiting
- Add tracing with Actuator/Prometheus
- Add Jib for building images
- Add Terraform for IaC
- Add GitHub Workflows for CI/CD for both code and infrastructure
- Setup SonarQube and connect it to GitHub Workflows
