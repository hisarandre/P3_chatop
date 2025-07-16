# P3 – Chatop

## 📌 Description

Chatop is a backend application for a real estate rental platform.  
It provides secure JWT-based authentication, CRUD management of rental listings, and messaging functionality related to those listings.

## 🚀 Features
- **Authentication:** Sign-up and login with JWT
- **User management:** Profile information retrieval
- **Rental listings:** Create, update, and read rental listings
- **Messaging:** Send messages linked to rentals
- **File upload:** Image upload and storage for listings
- **Security:** Endpoints protected with JWT tokens
- **API documentation:** Swagger / OpenAPI integration

## 🛠️ Technologies Used
- Java 21
- Spring Boot (Web, Security, Data JPA)
- JWT (JSON Web Tokens)
- MySQL
- Maven
- Lombok
- MapStruct
- Swagger / OpenAPI

## 🛠️ Architecture

```bash
src/
└── main/
    ├── java/com/chatop/backend/
    │   ├── configuration/       # Security and Swagger configuration
    │   ├── controller/          # REST controllers
    │   ├── dto/                 # Request/response DTOs
    │   ├── entity/              # JPA entities
    │   ├── exception/           # Custom exception handling
    │   ├── mapper/              # MapStruct mappers
    │   ├── repository/          # JPA repositories
    │   └── service/             # Business logic services
    └── resources/
        ├── application.properties
        └── schema.sql
```

## 🚀 Installation

### 🛠️ Prerequisites
- Java 21
- Maven
- MySQL 

### Setup & Run

#### 1. Clone the repository:

```bash
   git clone https://github.com/hisarandre/P3_chatop.git
   cd P3_chatop
```

#### 2. Configure the database:

- Make sure MySQL is installed and running
- Log in to your MySQL database 
- Create a database for the application `CREATE DATABASE chatop;`
- Import the schema.sql file located in the resources folder at the project root to initialize the schema
- Verify that the database contains the necessary tables after the import.

#### 3. Configure the environment:

Create a .env file in the root of your project by copying from the example file:

```bash
DATABASE_URL=your_database_url_here
DB_USERNAME=your_database_username_here
DB_PASSWORD=your_database_password_here
SECRET_KEY_JWT=your_jwt_secret_key_here
```

#### To make sure you use these environment variables when running the app:

- Open your project in IntelliJ IDEA.
- Go to Run > Edit Configurations… 
- Select your Spring Boot run configuration (or create a new one). 
- In the Environment variables field, click the ... button. 
- You can manually add each variable from your .env file, or if you installed the EnvFile plugin, you can select your .env file to load variables automatically. 
- Apply and save the configuration.

#### 4. Install Dependencies

```bash
mvn clean install
```
#### 5. Launch the backend:
```bash
mvn spring-boot:run
```

## 🗂️ Testing 
To fully test this project, make sure to run the front project or to use the postman collection

- **Frontend Angular**
Clone and set up the project available here:
[Chatop Frontend](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring)

- **Postman Collection:** 
A Postman collection is included in the project to test endpoints. It is located at the project root in the resources/chatop.postman_collection file. 

## 👉 API Documentation
Swagger UI is available here after starting up: http://localhost:3001/swagger-ui.html