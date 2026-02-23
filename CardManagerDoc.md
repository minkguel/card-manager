# 🎴 Pokemon Card Manager - Project Documentation

## 📖 Project Overview

The **Pokemon Card Manager** is a full-stack web application designed to help collectors catalog, organize, and manage their Pokemon card collections. The application provides an intuitive interface for adding cards with images, searching through collections, and sorting cards by various attributes.

### Purpose

This project serves as a personal collection management tool for Pokemon card enthusiasts who want to:
- Keep a digital inventory of their physical card collection
- Track card details including type, rarity, and acquisition date
- Search and filter their collection efficiently
- Store visual references (images) of each card
- Sort and organize cards by multiple criteria

---

## 🏗️ Architecture

The application follows a **three-tier architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (React)                     │
│              Vite + Tailwind CSS + React 19             │
│                  Port: 5173 (dev)                       │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP/REST API
                     │ (JSON + Multipart)
┌────────────────────▼────────────────────────────────────┐
│              Backend (Spring Boot)                      │
│         RESTful API + Business Logic                    │
│                  Port: 8080                             │
└────────────────────┬────────────────────────────────────┘
                     │ 
                     │
┌────────────────────▼────────────────────────────────────┐
│            Database (MongoDB)                           │
│              File-based persistence                     │
│            Location: cloud                              │
└─────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technology Stack

### Backend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 25 | Core programming language |
| **Spring Boot** | 4.0.1 | Application framework and dependency injection |
| **Spring Web MVC** | - | RESTful API development |
| **Spring Data JPA** | - | Database abstraction and ORM |
| **Hibernate** | - | JPA implementation for database operations |
| **H2 Database** | - | Embedded SQL database for data persistence |
| **Lombok** | - | Reduces boilerplate code (getters, setters, constructors) |
| **Maven** | - | Build automation and dependency management |

### Frontend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 19.2.0 | UI framework for building interactive interfaces |
| **Vite** | 7.2.4 | Fast build tool and development server |
| **Tailwind CSS** | 3.4.19 | Utility-first CSS framework for styling |
| **ESLint** | 9.39.1 | Code quality and linting |
| **PostCSS** | 8.5.6 | CSS processing and transformation |

### Development Tools

- **Maven Wrapper** (`mvnw`) - Ensures consistent Maven version across environments
- **Hot Module Replacement (HMR)** - Instant feedback during development via Vite
- **H2 Console** - Database inspection tool (accessible at `http://localhost:8080/h2-console`)

---

## 📂 Project Structure

```
pokemon-manager/
│
├── backend/
│   └── pokemon-manager/
│       ├── src/
│       │   └── main/
│       │       ├── java/com/mikkelpedersen/pokemonmanager/
│       │       │   ├── PokemonCard.java              # Entity model
│       │       │   ├── PokemonCardRepository.java    # Data access layer
│       │       │   ├── PokemonCardService.java       # Business logic layer
│       │       │   └── PokemonCardController.java    # REST API endpoints
│       │       └── resources/
│       │           └── application.properties        # Configuration
│       ├── data/                                     # H2 database files
│       ├── pom.xml                                   # Maven dependencies
│       └── mvnw                                      # Maven wrapper
│
└── frontend/
    ├── src/
    │   ├── App.jsx                                   # Main application component
    │   ├── App.css                                   # Component styles
    │   ├── index.css                                 # Global styles + Tailwind
    │   ├── main.jsx                                  # Application entry point
    │   └── assets/                                   # Static assets
    ├── public/                                       # Public static files
    ├── index.html                                    # HTML template
    ├── package.json                                  # npm dependencies
    ├── vite.config.js                                # Vite configuration
    └── tailwind.config.js                            # Tailwind configuration
```

---

## 🔧 Core Components

### Backend Components

#### 1. **PokemonCard Entity** (`PokemonCard.java`)
- **Purpose**: Defines the data model for a Pokemon card
- **Key Features**:
  - JPA entity with auto-generated ID
  - Stores card name, type, rarity
  - Binary image storage using `@Lob` annotation
  - Automatic date tracking with `@PrePersist`
- **Technologies**: JPA, Hibernate, Lombok

#### 2. **PokemonCardRepository** (`PokemonCardRepository.java`)
- **Purpose**: Data access layer for database operations
- **Key Features**:
  - Extends `JpaRepository` for CRUD operations
  - Custom search method: `findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase`
  - Automatic query generation by Spring Data JPA
- **Technologies**: Spring Data JPA

#### 3. **PokemonCardService** (`PokemonCardService.java`)
- **Purpose**: Business logic layer
- **Key Features**:
  - Card retrieval with optional sorting
  - Search functionality (name or type)
  - Card creation with image upload handling
  - Card deletion
- **Technologies**: Spring Service, MultipartFile handling

#### 4. **PokemonCardController** (`PokemonCardController.java`)
- **Purpose**: REST API endpoints
- **Endpoints**:
  - `GET /api/cards` - Retrieve all cards (with optional sorting and search)
  - `POST /api/cards` - Create new card with image upload
  - `DELETE /api/cards/{id}` - Delete card by ID
- **Key Features**:
  - CORS configuration for frontend communication
  - Multipart form data handling for image uploads
- **Technologies**: Spring REST, @CrossOrigin

### Frontend Components

#### 1. **App Component** (`App.jsx`)
- **Purpose**: Main application logic and UI
- **Key Features**:
  - State management for cards, search, and sorting
  - API integration with backend
  - Modal for adding new cards
  - Responsive card grid display
  - Real-time search filtering
- **Technologies**: React Hooks (useState, useEffect)

#### 2. **Styling** (`index.css` + `App.css`)
- **Purpose**: Application styling
- **Key Features**:
  - Tailwind CSS utilities
  - Custom component styles
  - Responsive design
  - Modern UI aesthetics

---

## 🔄 Data Flow

### Adding a New Card

```
User fills form → Frontend validates → 
FormData created (multipart) → 
POST /api/cards → 
Controller receives request → 
Service processes image → 
Repository saves to DB → 
Response sent to frontend → 
UI updates with new card
```

### Searching Cards

```
User types in search box → 
useEffect triggers → 
GET /api/cards?search=query → 
Service searches by name/type → 
Repository executes query → 
Results returned → 
Frontend filters and displays
```

### Sorting Cards

```
User selects sort option → 
State updates → 
GET /api/cards?sortBy=field → 
Repository applies sorting → 
Sorted results returned → 
UI re-renders with sorted cards
```

---

## 💾 Database Schema

### PokemonCard Table

| Column | Type | Description | Constraints |
|--------|------|-------------|-------------|
| `id` | BIGINT | Primary key | AUTO_INCREMENT, NOT NULL |
| `name` | VARCHAR | Card name | - |
| `type` | VARCHAR | Pokemon type (e.g., Fire, Water) | - |
| `rarity` | VARCHAR | Card rarity level | - |
| `image` | BLOB | Card image binary data | - |
| `date_added` | DATE | Date card was added | Auto-set on creation |

**Database Type**: H2 (embedded, file-based)  
**Location**: `./data/pokemonDB`  
**Dialect**: H2Dialect  
**DDL Strategy**: `update` (auto-creates/updates schema)

---

## 🎨 Key Features

### 1. **Card Management**
- Add new cards with complete details
- Upload and store card images (up to 10MB)
- Delete cards from collection
- Automatic date tracking

### 2. **Search & Filter**
- Real-time search by card name
- Search by Pokemon type
- Case-insensitive matching
- Instant results

### 3. **Sorting Options**
- Sort by name (alphabetical)
- Sort by type
- Sort by rarity
- Sort by date added

### 4. **Image Handling**
- Binary storage in database
- Base64 encoding for display
- Multipart form upload
- 10MB file size limit

### 5. **User Interface**
- Responsive grid layout
- Modal-based card creation
- Modern, clean design
- Mobile-friendly

---

## 🚀 Running the Application

### Backend Setup

```bash
cd backend/pokemon-manager

# Run with Maven wrapper
./mvnw spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/pokemon-manager-0.0.1-SNAPSHOT.jar
```

**Backend will be available at**: `http://localhost:8080`  
**H2 Console**: `http://localhost:8080/h2-console`

### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build
```

**Frontend will be available at**: `http://localhost:5173`

---

## 🔐 Configuration

### Backend Configuration (`application.properties`)

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:h2:file:./data/pokemonDB
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File Uploads
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# H2 Console
spring.h2.console.enabled=true
```

### Frontend Configuration

- **API Base URL**: `http://localhost:8080/api/cards`
- **Dev Server Port**: 5173 (configurable in `vite.config.js`)
- **CORS**: Enabled for `http://localhost:5173`

---

## 📊 API Endpoints

### GET `/api/cards`
**Description**: Retrieve all cards with optional filtering and sorting

**Query Parameters**:
- `sortBy` (optional): Field to sort by (name, type, rarity, dateAdded)
- `search` (optional): Search query for name or type

**Response**: `200 OK` with array of PokemonCard objects

---

### POST `/api/cards`
**Description**: Create a new card with image upload

**Content-Type**: `multipart/form-data`

**Form Parameters**:
- `name` (required): Card name
- `type` (required): Pokemon type
- `rarity` (required): Card rarity
- `image` (optional): Image file (max 10MB)

**Response**: `201 CREATED` with created PokemonCard object

---

### DELETE `/api/cards/{id}`
**Description**: Delete a card by ID

**Path Parameters**:
- `id`: Card ID to delete

**Response**: `204 NO CONTENT`

---

## 🎯 Design Patterns Used

1. **MVC (Model-View-Controller)**
   - Model: `PokemonCard` entity
   - View: React frontend components
   - Controller: `PokemonCardController`

2. **Repository Pattern**
   - `PokemonCardRepository` abstracts data access

3. **Service Layer Pattern**
   - `PokemonCardService` encapsulates business logic

4. **Dependency Injection**
   - Spring's `@RequiredArgsConstructor` with Lombok
   - Constructor-based injection

5. **RESTful API Design**
   - Resource-based URLs
   - HTTP methods for CRUD operations
   - Proper status codes

---

## 🔍 Supported Card Types

- Grass
- Fire
- Water
- Electric
- Psychic
- Fighting
- Darkness
- Metal
- Fairy
- Dragon
- Normal

## 🏆 Supported Rarity Levels

- Common
- Uncommon
- Rare
- Rare Holo
- Ultra Rare
- Secret Rare
- Promo
- Custom

---

## 🧪 Development Features

### Backend
- **SQL Logging**: Enabled via `spring.jpa.show-sql=true`
- **H2 Console**: Database inspection and querying
- **Hot Reload**: Spring Boot DevTools support
- **Auto-schema Management**: Hibernate DDL auto-update

### Frontend
- **Hot Module Replacement**: Instant UI updates
- **ESLint**: Code quality enforcement
- **Vite**: Lightning-fast builds and HMR
- **React DevTools**: Component inspection

---

## 📈 Future Enhancement Possibilities

- User authentication and authorization
- Multiple collection support
- Card value tracking
- Export/import functionality
- Advanced filtering (by set, year, etc.)
- Card condition tracking
- Statistics and analytics dashboard
- Cloud storage integration
- Mobile app version
- Trading/wishlist features

---

## 👨‍💻 Developer Information

**Project Type**: Personal Portfolio Project  
**Purpose**: Pokemon card collection management and full-stack development demonstration  
**Architecture**: Monorepo with separate frontend and backend

---

## 📄 License

This is a personal project for portfolio and collection management purposes.

---

## 🤝 Contributing

This is a personal project, but suggestions and feedback are welcome!

---

*Last Updated: February 2026*
