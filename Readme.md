# GitHub V3 API Service

## Overview

A simple REST API to fetch a user's GitHub repositories (excluding forks).  

## Technologies

- Java 21+  
- Spring Boot 3.4.4  
- Jackson  
- Lombok  

## Setup & Run

```sh
git clone https://github.com/OlinykFS/github-v3-api

mvn clean install

mvn spring-boot:run
```

## API Endpoints

### 🔹 Get user repositories (excluding forks)
```http
GET /api/v1/{username}/repositories
```

**Example Request:**
```sh
curl -X GET http://localhost:8080/api/v1/octocat/repositories
```

**Example Response:**
```json
{
  "name": "hello-worId",
  "owner": "octocat",
  "branches": [
    {
      "name": "master",
      "lastCommitSha": "7e068727fdb347b685b658d2981f8c85f7bf0585"
    }
  ]
}
```