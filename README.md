# Project Fetcher

A Spring Boot application that aggregates and caches GitHub contributions (pull requests, issues, and commits) for a specified user using the GitHub GraphQL API.

## Features

- **Contribution Aggregation**: Fetches and aggregates GitHub contributions including:
  - Pull Requests
  - Issues
  - Commits
- **Automatic Caching**: Refreshes contribution data every 10 minutes
- **Historical Data**: Retrieves contributions from the last 5 years
- **REST API**: Provides a simple endpoint to access cached contribution data
- **GraphQL Integration**: Uses GitHub's GraphQL API for efficient data fetching
- **Reactive Programming**: Built with Spring WebFlux for non-blocking operations

## Technology Stack

- **Java 25**
- **Spring Boot 4.0.2**
- **Spring WebMVC** - REST API endpoints
- **Spring WebFlux** - Reactive HTTP client for GitHub API
- **Lombok** - Reduce boilerplate code
- **GraalVM Native Image** - Optional native compilation support

## Prerequisites

- Java 25 or higher
- Maven 3.6+
- GitHub Personal Access Token

## Configuration

Create a `.env` file or set the following environment variable:

```properties
github.username=your-github-username
```

You'll also need to configure a GitHub Personal Access Token for API access. Add it to your `application.properties`:

```properties
github.token=your-github-personal-access-token
```

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd projectFetcher
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

## Usage

Once the application is running, access the contributions data via:

```
GET http://localhost:8080/api/contributions
```

This endpoint returns a map of contributions grouped by date, including all pull requests, issues, and commits from the configured GitHub user over the last 5 years.

### Response Format

The API returns contributions organized by date:

```json
{
  "2024-01-15": [
    {
      "type": "PULL_REQUEST",
      "repository": "owner/repo",
      "title": "Feature: Add new feature",
      ...
    }
  ]
}
```

## Architecture

The application consists of several key components:

- **ContributionController**: REST API endpoint
- **GitHubContributionService**: Main service orchestrating contribution fetching and caching
- **PullRequestContributionService**: Fetches PR contributions
- **IssueContributionService**: Fetches issue contributions
- **CommitContributionService**: Fetches commit contributions
- **ContributionAggregationService**: Aggregates contributions by date
- **GitHubGraphQLClient**: Handles GraphQL API communication

## Building Native Image

The project includes GraalVM Native Image support:

```bash
./mvnw -Pnative native:compile
```

## Development

The application automatically refreshes the contribution cache every 10 minutes. The initial cache is populated on the first API request if not already loaded.

## License

This project is licensed under the terms specified in the `pom.xml` file.

## Author

Janne6565
