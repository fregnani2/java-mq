## Overview
Transaction API integrated with IBM MQ.\
It uses Spring Boot, Spring Data JPA,Spring Security, JMS, H2 database, Lombok and IBM MQ.\
You can register Clients and give them Client or Admin roles, only Admins can delete a client and get a list of all clients.


## Getting Started
### Prerequisites
- Docker

### Step-by-step guide
1. Clone the repository.
2. Run a IBM MQ container
```shell
docker pull icr.io/ibm-messaging/mq:latest
docker volume create qm1data
docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --publish 1414:1414 --publish 9443:9443 --detach ibmcom/mq
```
3. Run the docker-compose file
```shell
docker compose up --build
```
4. Connect the containers to the same network
```shell
docker network create mq_network
docker network connect mq_network <mq Container name>
docker network connect mq_network <api Container name>
```
* You can check the container names with `docker ps -a`
## Base URL
The base URL  is `http://localhost:8080/`

## Endpoints
### Create a client
#### Request
```http
POST /client/register
```
```json
{
  "name": "Test",
  "accountNumber": 12,
  "balance": 5000,
  "role": "ADMIN",
  "password": "123",
  "email": "test@gmail.com"

}
```
### Login
#### Request
```http
POST /client/
```
```json
{
  "email": "test@gmail.com",
  "password": "123"
}
```
### Get all clients
#### Request
```http
GET /client
```

### Get a client by account number
#### Request
```http
GET /client/{accountNumber}
```

### Make a transaction
#### Request
```http
POST /transaction
```
```json
{
  "toAccountNumber": 220,
  "fromAccountNumber": 123,
  "amount": 50
}
```
### Get Transaction by account number
#### Request
```http
GET /transaction/{accountNumber}
```

### Update a client
#### Request
```http
PUT /client/{accountNumber}
```
```json
{
  "name": "Test",
  "accountNumber": 12,
  "balance": 5000,
  "role": "ADMIN",
  "password": "123",
  "email": "test@gmail.com"

}
```

### Delete a client
#### Request
```http
DELETE /client/{accountNumber}
```
## Insomnia Collection
You can download the Insomnia collection [here](https://github.com/fregnani2/java-mq/blob/main/collection.json) to test the API.

