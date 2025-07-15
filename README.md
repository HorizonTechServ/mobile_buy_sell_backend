# 🎓 Arpit Institute Backend API

This is a RESTful API for **Arpit Institute**, designed to manage students, receipts, and administrative operations efficiently. The backend provides secure, role-based access and handles student registration, fee collection, receipt generation, and user management.

---

## 🚀 Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* MySQL
* JWT (Authentication & Authorization)
* Gradle
* Swagger for API Documentation

---

## 🧩 Modules

### 🧑‍🏫 User Management

* Register Admin, and Students
* Update user details and profile picture
* Reset/change password using OTP
* Role-based access controls

### 💵 Receipt/Billing Module

* Generate and store tuition receipts
* Calculate fees and total dynamically
* Convert amount to words
* Attach cheque/payment details
* Retrieve receipt by ID
* List all receipts

### 📦 Authentication

* JWT-based login/logout
* Token validation
* Secure password encryption (BCrypt)

---

## 🔐 Role-Based Access

| Role           | Capabilities                                          |
| -------------- | ----------------------------------------------------- |
| 🔵 Admin       | Register/edit students, manage receipts               |
| 🔹 Student     | View profile, update password, upload profile image   |

---

## 🎯 Key Features

* ✅ Secure JWT authentication & role-based authorization
* ✅ Admins can register/update students
* ✅ Students can view and update profile
* ✅ Dynamic fee receipt generation
* ✅ Fee breakup with amount-to-words converter
* ✅ Soft delete support for users

---

## ⚙️ Installation & Run

```bash
# Clone the repository
git clone https://github.com/HorizonTechServ/arpit-institute-backend.git
cd arpit-institute-backend

# Run using Gradle
./gradlew bootRun
```

Ensure `MySQL` is running and the database is configured in `application.yml`.

---

## 📄 Swagger API Docs

Access all API documentation via Swagger UI:

📌 (http://localhost:9191/institute/api/swagger-ui/index.html#/)

---
