
# OptiMart — Smart Shopping Management System

OptiMart is a Java-based e-commerce management application designed to simplify online shopping operations through an interactive desktop interface and a console-based management system.

The application provides a complete shopping workflow, including product browsing, searching, sorting, user authentication, shopping cart management, order placement, order tracking, and administrative operations.

Built with Java, JavaFX, Maven, and custom data structures, OptiMart demonstrates the practical implementation of object-oriented programming, data structures, file handling, and GUI development in a real-world shopping management scenario.

---

## Overview

OptiMart provides two application interfaces:

- **Graphical User Interface (GUI):** A desktop shopping experience developed using JavaFX.
- **Console Interface:** A menu-driven application for managing customers, products, shopping carts, orders, and deals.

The system supports separate customer and administrator experiences, allowing users to access functionality based on their assigned roles.

## Features

### Customer Features

- User registration and login.
- Browse available products.
- Search products using an autocomplete search interface.
- Sort products by name and price.
- View detailed product information.
- Add products to the shopping cart.
- Remove items from the cart.
- Update product quantities.
- Undo and redo cart operations.
- View cart contents and calculate totals.
- Apply available product deals.
- Place orders.
- View order history.
- Track order status.
- Change account password.

### Administrator Features

- Dedicated administrator dashboard.
- Add new products.
- Update existing product information.
- Delete products.
- View the complete product catalog.
- Search and sort products.
- Manage registered users.
- Promote customers to administrator roles and vice versa.
- View all customer orders.
- Update order statuses.
- Create and manage promotional deals.
- Remove expired deals.
- View system statistics.
- Change administrator password.

### Shopping and Product Management

- Product identification through unique product IDs.
- Product categorization.
- Stock and inventory management.
- Product price management.
- Product descriptions and details.
- Discount calculations.
- Deal expiration and stock limits.
- Persistent storage using JSON files.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 23 | Core programming language |
| JavaFX 17.0.6 | Desktop graphical user interface |
| JavaFX Controls | GUI components and interactive controls |
| JavaFX FXML | JavaFX UI support |
| Maven | Dependency management and build automation |
| Gson 2.10.1 | JSON serialization and deserialization |
| JUnit 5.10.2 | Unit testing framework |
| JSON | Local application data storage |

---

## Data Structures and Algorithms

A key aspect of OptiMart is the implementation of custom data structures and algorithms rather than depending entirely on Java's built-in collections.

### Custom Data Structures

**1. Custom HashMap**

Used for key-value-based data management, including product lookup and user cart management.

**2. Custom LinkedList**

Provides dynamic collection management for application data, including product collections.

**3. Stack**

Supports stack-based operations, particularly undo and redo functionality in shopping cart management.

**4. Trie**

Supports efficient text-based searching and autocomplete functionality.

**5. Node and TrieNode**

Supporting classes used to build the linked-list and trie implementations.

### Algorithms

- Merge Sort for product sorting.
- Sorting products alphabetically by name.
- Sorting products by price in ascending order.
- Sorting products by price in descending order.
- Trie-based search and autocomplete.
- Stack-based undo and redo operations.
- Hash-based product and cart lookup.

---

## Project Architecture

OptiMart follows a modular Java project structure that separates the graphical interface, business logic, data models, custom data structures, and utility functionality.

```text
OptiMart/
│
├── data/
│   ├── products.json
│   ├── users.json
│   ├── orders.json
│   └── deals.json
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── module-info.java
│       │   │
│       │   └── com/optimart/
│       │       │
│       │       ├── Actions/
│       │       │   └── CartAction.java
│       │       │
│       │       ├── App/
│       │       │   └── Main.java
│       │       │
│       │       ├── DataStructures/
│       │       │   ├── HashMap.java
│       │       │   ├── LinkedList.java
│       │       │   ├── Node.java
│       │       │   ├── Stack.java
│       │       │   ├── Trie.java
│       │       │   └── TrieNode.java
│       │       │
│       │       ├── GUI/
│       │       │   ├── Components/
│       │       │   │   ├── AutocompleteSearchField.java
│       │       │   │   ├── ProductCard.java
│       │       │   │   └── TopBar.java
│       │       │   │
│       │       │   ├── Views/
│       │       │   │   ├── AdminDashboardView.java
│       │       │   │   ├── AdminDealsManagement.java
│       │       │   │   ├── AdminOrderManagementView.java
│       │       │   │   ├── CartView.java
│       │       │   │   ├── CustomerDashboardView.java
│       │       │   │   ├── DealsView.java
│       │       │   │   ├── HomeView.java
│       │       │   │   ├── LoginView.java
│       │       │   │   ├── MainMenuView.java
│       │       │   │   ├── OrderHistoryView.java
│       │       │   │   ├── OrderTrackingView.java
│       │       │   │   ├── PasswordChangeView.java
│       │       │   │   ├── ProductDetailView.java
│       │       │   │   ├── ProductSortView.java
│       │       │   │   ├── SearchView.java
│       │       │   │   ├── SignupView.java
│       │       │   │   └── SimpleMessageView.java
│       │       │   │
│       │       │   ├── OptimartApp.java
│       │       │   └── Theme.java
│       │       │
│       │       ├── Models/
│       │       │   ├── Cart.java
│       │       │   ├── CartItem.java
│       │       │   ├── Deal.java
│       │       │   ├── Order.java
│       │       │   ├── Product.java
│       │       │   └── User.java
│       │       │
│       │       ├── Services/
│       │       │   ├── DealService.java
│       │       │   ├── OrderStatusService.java
│       │       │   ├── ProductServices.java
│       │       │   └── UserServices.java
│       │       │
│       │       └── Utils/
│       │           ├── FileHandler.java
│       │           ├── PassUtil.java
│       │           └── ProductSorter.java
│       │
│       │       └── resources/
│       │           └── images/
│       │               ├── logo.png
│       │               ├── appliances.png
│       │               ├── books.png
│       │               ├── fashion.png
│       │               ├── headphones.png
│       │               ├── laptop.png
│       │               └── smartphone.png
│
├── .mvn/
│   └── wrapper/
│
├── mvnw
├── mvnw.cmd
├── pom.xml
└── .gitignore
```

---

## Project Structure Explanation

| Directory / File | Description |
|---|---|
| `Actions/` | Contains action-related functionality, including cart operations. |
| `App/` | Contains the console application entry point. |
| `DataStructures/` | Implements custom HashMap, LinkedList, Stack, and Trie. |
| `GUI/` | Contains the JavaFX graphical application. |
| `GUI/Components/` | Reusable GUI components such as product cards, top bar, and autocomplete search. |
| `GUI/Views/` | Contains individual application screens for customers and administrators. |
| `Models/` | Defines application entities such as Product, User, Cart, Deal, and Order. |
| `Services/` | Implements business logic for products, users, promotional deals, and order tracking. |
| `Utils/` | Contains file handling, password validation, and product sorting utilities. |
| `resources/images/` | Contains application images and category graphics. |
| `data/` | Stores application information in JSON format. |
| `pom.xml` | Defines project dependencies, Java compiler configuration, and Maven plugins. |

---

## Getting Started

Follow these instructions to download, configure, and run OptiMart locally.

### Prerequisites

Make sure the following tools are installed:

- Java Development Kit (JDK) 23.
- Apache Maven, or use the included Maven Wrapper.
- IntelliJ IDEA or another Java IDE.
- Git.

You can verify your Java and Maven installations using:

```bash
java -version
mvn -version
```

### 1. Clone the Repository

```bash
git clone https://github.com/muhammad-saad-2006/OptiMart.git
```

### 2. Navigate to the Project Directory

```bash
cd OptiMart
```

### 3. Build the Project

Using Maven:

```bash
mvn clean package
```

Alternatively, use the included Maven Wrapper.

**Windows:**

```bash
mvnw.cmd clean package
```

**Linux / macOS:**

```bash
./mvnw clean package
```

### 4. Run the JavaFX Application

To launch the graphical desktop application:

```bash
mvn javafx:run
```

**Windows using Maven Wrapper:**

```bash
mvnw.cmd javafx:run
```

**Linux / macOS using Maven Wrapper:**

```bash
./mvnw javafx:run
```

The JavaFX application starts through:

```text
com.optimart.GUI.OptimartApp
```

### 5. Run the Console Application

The repository also contains a separate console-based application.

Its entry point is:

```text
com.optimart.App.Main
```

Run the `Main.java` class from your IDE, ensuring that the project dependencies and module configuration are available.

---

## Application Data

OptiMart uses JSON files for local data persistence.

The `data/` directory contains:

| File | Description |
|---|---|
| `products.json` | Stores product IDs, names, categories, prices, stock quantities, and descriptions. |
| `users.json` | Stores registered user information and role assignments. |
| `orders.json` | Stores customer orders, purchased products, quantities, totals, and order statuses. |
| `deals.json` | Stores promotional deal information. |

The application uses the `FileHandler` utility to load and save application data.

**Important:** The JSON files contain sample application data. Back up these files before experimenting with changes or resetting the application.

---

## Security Note

This project is designed as an educational demonstration of an e-commerce management system.

The current JSON-based implementation should not be considered production-ready authentication or secure database storage.

For production deployment, the following improvements are recommended:

- Store passwords using a secure password-hashing algorithm such as BCrypt or Argon2.
- Remove sample credentials from publicly distributed repositories.
- Use a proper database with access controls.
- Implement stronger session and authorization management.
- Validate user input and sensitive operations.

---

## Testing

JUnit 5 is included as the project's testing framework.

To execute the test suite:

```bash
mvn test
```

To run a complete build:

```bash
mvn clean verify
```

---

## Learning Objectives

This project demonstrates the practical application of several computer science concepts:

- Object-Oriented Programming (OOP).
- Data Structures and Algorithms.
- Custom HashMap implementation.
- Linked List implementation.
- Stack operations.
- Trie-based searching.
- Merge Sort.
- JavaFX GUI development.
- Event-driven programming.
- File handling and JSON persistence.
- Modular Java application development.
- Maven dependency management.
- Basic software architecture and separation of concerns.

---

## Future Improvements

Potential enhancements for future versions include:

- Integration with a relational database such as MySQL or PostgreSQL.
- Improved authentication and password security.
- Advanced inventory reporting.
- Product reviews and ratings.
- Wishlist functionality.
- Payment gateway integration.
- Email notifications for order updates.
- Advanced analytics for administrators.
- Automated testing and expanded test coverage.

---

## Author

**Muhammad Saad**

GitHub: [@muhammad-saad-2006](https://github.com/muhammad-saad-2006)

Project Repository: [OptiMart](https://github.com/muhammad-saad-2006/OptiMart)

---

**OptiMart — Your Smart Shopping Management Solution.**
