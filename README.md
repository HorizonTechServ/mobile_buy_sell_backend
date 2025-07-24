# 📱 Mobile Buy & Sell Backend API

This is a RESTful API for a **Mobile Buy & Sell** application. It allows users to add mobile buying and selling entries, view inventory, and generate bills for sold products. The backend is secure, scalable, and built using modern Java Spring technologies.

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

### 👤 User Management

* Register users
* Update user details and profile picture
* Reset/change password using OTP
* Role-based access controls

### 📱 Buy & Sell Module

* Add buy entry for mobile devices
* Add sell entry for available inventory
* View all bought mobiles
* View all sold mobiles
* Generate detailed bill for each sale

### 🔐 Authentication

* JWT-based login/logout
* Token validation
* Secure password encryption (BCrypt)

---

## 🔐 Role-Based Access

| Role           | Capabilities                                          |
| -------------- | ----------------------------------------------------- |
| 🔵 Admin       | Add/view buy & sell entries, generate bills           |
| 🔹 User        | View own buy/sell records, update profile             |

---

## 🎯 Key Features

* ✅ Secure JWT authentication & role-based authorization
* ✅ Users can record buy and sell transactions
* ✅ Admins can generate bills for sold items
* ✅ Detailed inventory view (buy/sell listing)
* ✅ Soft delete support for records
* ✅ Audit timestamps for creation and updates

---

## ⚙️ Installation & Run

```bash
# Clone the repository
git clone https://github.com/HorizonTechServ/mobile_buy_sell_backend.git
cd mobile-buy-sell-backend

# Run using Gradle
./gradlew bootRun
