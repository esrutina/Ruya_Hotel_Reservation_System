🏨 Ruya Hotel Reservation System

A desktop-based Hotel Reservation System built using **Java Swing** and **MySQL**.  
This system provides a complete solution for managing hotel operations including room booking, customer management, payments, and administration.

---

## 🚀 Features

### 👤 User Side
- User registration and login
- Browse available rooms
- Room booking system
- View booking history
- Make payments
- Submit feedback

### 🛠️ Admin Side
- Dashboard overview
- Manage rooms (Add / Update / Delete)
- Manage users
- Manage reservations
- Payment tracking
- Reports generation
- Feedback management

---

## 💻 Technologies Used

- Java (JDK 8+)
- Java Swing (GUI)
- MySQL Database
- JDBC (Database Connection)
- FlatLaf UI Theme
- JCalendar (Date picker)
- JSON library

---

## 📂 Project Structure


src/
└── com.ruyahotel/
├── dao/
├── model/
├── service/
├── ui/
│ ├── admin/
│ ├── customer/
│ └── publicpages/
└── util/


---

## ⚙️ Setup Instructions

1. Clone the repository:
```bash
git clone https://github.com/esrutina/Ruya_Hotel_Reservation_System.git
Open project in IntelliJ / NetBeans / Eclipse
Import MySQL database:
database/ruya_hotel.sql
Configure database connection in:
src/com/ruyahotel/util/AppConfig.java
Run:
Main.java
🔐 Security Note

This project previously contained API keys which have been removed for security reasons.
Environment variables should be used for sensitive data in production.

👨‍💻 Author

Esrael Enyew
Software Engineering Student
Future Full Stack Developer 🚀

📌 License

This project is for educational purposes.
