# Flights Management System

## 📋 Overview

**Flights Management System** is a desktop JavaFX application designed for managing flights, passengers, and reservations.  
It provides a user-friendly graphical interface to view, search, add, update, and delete records while enforcing business rules at both the application and database levels.

---

## ✈️ Features

### Flights
- Manage flight records (departure, destination, time, duration, seats, direction)
- Automatic generation of seat layout
- Trigger-based management of seat availability
- Search by ID, date, departure, or destination

### Passengers
- Add, search, update and delete passenger data
- Basic data validation (name, email, phone)

### Reservations
- Create, update, delete reservations
- Display full reservation details with passenger info
- Automatic email confirmation for reservations
- Checks if reservation took place based on departure date

---

## ⚙️ Technologies Used

- **Java 21**
- **JavaFX 21** (FXML, TableView, ComboBox, DatePicker)
- **Maven** (project build and dependency management)
- **H2 Database** (in file & in-memory, with SQL triggers)
- **HikariCP** (connection pooling)
- **JUnit 5 + Mockito** (for unit testing)
- **JaCoCo** (code coverage reporting)
- **Javadoc** (documentation generating)

---

## 🗃️ Database

- Uses **H2 SQL** engine
- Schema and data initialization from `resources/db/schema.sql` and `data.sql`
- Integrated triggers:
  - `MakeSeatUnavailableTrigger` – after reservation insert
  - `MakeSeatAvailableTrigger` – after reservation delete
  - `ManageSeatAvailabilityAfterUpdateTrigger` – after reservation update
- Triggers automatically update seat availability in `seats` table

---

## 🧪 Testing

### Unit Testing:
- Service layer tested with **Mockito**
- DAO tested with **in-memory H2**

### Test:
```bash
mvn clean test
```
To generate Code Coverage Report replace `test` with `verify`, report will be generated at:
```
target/site/jacoco/index.html
```

---

## 🧭 Architecture

- **MVC-based layered structure**
- Controllers: handle GUI logic only
- Services: contain validation and business rules
- DAOs: raw SQL and mapping via `ResultSetMapper`
- Models: `Flight`, `Passenger`, `Reservation`, `Seat`
- Utility classes for UI (`ControllerUtils`), email (`EmailService`)

---

## ▶️ Run the Application

**Remember!** Before running the application you need to configure Email Service (see **Email Configuration** section)

```bash
mvn clean javafx:run
```

> GUI will launch from `MainApp.fxml`. Navigation through flights, passengers, and reservations is provided via `MenuView`.

---

## 📂 Project Structure

```
src/
 └── main/
     ├── java/lot/
     │   ├── controllers/
     │   ├── services/
     │   ├── dao/
     │   ├── models/
     │   └── utils/
     └── resources/
         ├── db/
         ├── views/
         └── css/
```

---

## 📧 Email Configuration

Email sending is handled by `EmailService`.  
To enable real emails, configure SMTP properties. To do so, create `.env` file and inside declare required properties, e.g. for gmail:
```
EMAIL_SMTP_HOST=smtp.gmail.com
EMAIL_SMTP_PORT=587
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_password
```


---

## 📦 Packaging

To build a `.jar`:

```bash
mvn clean package
```

---

## 🧠 Author Notes

This application demonstrates clean separation of concerns, strong testability, and integration of GUI with logic and persistence.  
Business rules are enforced both in Java and directly in the database through triggers.
