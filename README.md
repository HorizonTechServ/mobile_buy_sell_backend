# Society Management API

This is a RESTful API for a **Society Management System**, designed to streamline residential society operations. The system supports role-based access for **Super Admin**, **Admin**, and **User** and includes modules for user management, property (flat/house) handling, and complaint tracking.

---

## 🚀 Tech Stack

- Java  
- Spring Boot  
- Spring Security  
- Spring Data JPA  
- Hibernate  
- MySQL  
- JWT (for Authentication)  
- Gradle  

---

## 🧩 Modules

- **Authentication Module** (JWT-based login/logout)
- **User Management Module** (Super Admin, Admin, and User operations)
- **Property Module** (Flat and House management)
- **Complaint Module** (User complaints against society issues)

---

## 🔐 Role-Based Access

### 🟣 Super Admin
- Can create and manage Admin accounts
- Full system access and oversight

### 🔵 Admin
- Can register and manage Users
- Can add/edit/delete Flats and Houses

### 🟢 User
- Can log in and view assigned property details
- Can raise complaints related to society issues

---

## 🎯 Features

- Secure **JWT-based authentication** and authorization
- Clean role-based access control
- CRUD operations for Flats, Houses, Users, and Complaints
- Admins and Super Admins can manage user hierarchies
- Users can submit and track society complaints

---

## ⚙️ Installation & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/HorizonTechServ/society-mate-backend.git
   cd society-mate-backend

## ⚙️ Swagger URL

http://localhost:9090/society/api/swagger-ui/index.html#


