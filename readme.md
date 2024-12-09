# TaskManager

TaskManager is a full-stack application that combines a Spring Boot backend with a MySQL database and an Android frontend. This application allows users to create accounts, log in, and manage tasks efficiently.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
  - [1. Install Required Software](#1-install-required-software)
  - [2. Clone the Repository](#2-clone-the-repository)
  - [3. Import the Project into Eclipse](#3-import-the-project-into-eclipse)
  - [4. Set Up the MySQL Database](#4-set-up-the-mysql-database)
  - [5. Configure and Run the Backend](#5-configure-and-run-the-backend)
  - [6. Run the Android Application](#6-run-the-android-application)
- [Usage](#usage)
- [Troubleshooting](#troubleshooting)

## Features

- **User Authentication:** Create and manage user accounts.
- **Task Management:** Add, view, update, and delete tasks.
- **Responsive Android App:** Intuitive interface for managing tasks on the go.
- **Secure Backend:** Robust Spring Boot API with MySQL database integration.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Operating System:** Windows, macOS, or Linux
- **Java Development Kit (JDK):** Version 17
- **Android Studio:** Latest version
- **Eclipse IDE:** Latest version with Maven support
- **MySQL:** Version 8.0.37
- **MySQL Workbench:** Latest version
- **Git:** Installed and configured on your system

## Installation

Follow these steps to set up the TaskManager application on your local machine.

### 1. Install Required Software

Ensure the following software is installed on your system:

- **Android Studio:** [Download](https://developer.android.com/studio)
- **Eclipse IDE:** [Download](https://www.eclipse.org/downloads/)
- **MySQL:** [Download](https://dev.mysql.com/downloads/mysql/8.0.html)
- **MySQL Workbench:** [Download](https://dev.mysql.com/downloads/workbench/)
- **Git:** [Download](https://git-scm.com/downloads)

### 2. **Import Android Studio Project:**

In Android Studio click `get from vsc` paste `https://github.com/cblankenback/TaskManager`

or

  - Open Android Studio.
   - Click on `File` > `New` > `Project from Version Control`.
   - Paste the repository link: `https://github.com/cblankenback/TaskManager` and follow the prompts to clone the project.


### 3. Import the Project into Eclipse

1. **Open Eclipse:**
   Launch the Eclipse IDE.

2. **Import Maven Project:**
   - Navigate to `File` > `Import`.
   - Select `Maven` > `Existing Maven Projects` and click `Next`.
   - Click `Browse` and navigate to the cloned `Android Studio` project directory.
   - Find `SpringBootTaskManagerAPI` folder
   - Click `taskmanager` and import it.



### 4. Set Up the MySQL Database

1. **Start MySQL Server:**
   Ensure your MySQL server is running.

2. **Initialize the Database:**
   - In Eclipse, locate the `db` folder within the `SpringBootTaskManagerAPI` directory.
   - Open the `TaskManagerDB.sql` file.
   - Execute the SQL script in MySQL Workbench to set up the necessary tables and initial data.

3. **Create Database and User:**
   - Open MySQL Workbench.
   - Connect to your MySQL server.
   - Run the following SQL commands to create the database and user:

     ```sql
     CREATE USER 'cst3115'@'localhost' IDENTIFIED BY '3115';

     GRANT ALL PRIVILEGES ON TaskManager.* TO 'cst3115'@'localhost';

     FLUSH PRIVILEGES;
     ```



### 5. Configure and Run the Backend

1. **Open the Backend Project:**
   Open Eclipse

2. **Build the Project:**
   - Right-click on `TaskManagerApplication.java` located in `com.example.taskmanager`.
   - Select `Run As` > `Maven install`.
   - Wait for the build process to complete successfully.

3. **Run the Application:**
   - After the build, right-click on `TaskManagerApplication.java` again.
   - Select `Run As` > `Java Application`.
   - The Spring Boot application should start, initializing the backend server.

### 6. Run the Android Application

1. **Open Android Studio:**
   Launch Android Studio and open the cloned TaskManager project if not already open.

2. **Build the Project:**
   - Click on `Build` > `Make Project` to compile the Android application.

3. **Configure Emulator or Device:**
   - Set up an Android emulator or connect an Android device via USB with USB debugging enabled.

4. **Run the App:**
   - Click the `Run` button or press `Shift + F10`.
   - Select your target device or emulator.
   - The app will install and launch on the selected device.

## Usage

1. **Create an Account:**
   - Open the Android app.
   - Navigate to the registration screen and create a new account using your email and a secure password.

2. **Log In:**
   - Use your credentials to log in to the app.

3. **Manage Tasks:**
   - Add new tasks by providing necessary details.
   - View your list of tasks.
   - Update or delete tasks as needed.

## Troubleshooting

- **Database Connection Issues:**
  - Ensure MySQL server is running.
  - Verify the database credentials in the application's `application.properties` or `application.yml` file.

- **Port Conflicts:**
  - If the backend server fails to start, check if the default port (e.g., 8080) is in use and change it if necessary.

- **Android App Not Connecting to Backend:**
  - Ensure the backend server is running and accessible.
  - Verify the API base URL in the Android application's configuration matches the backend server address.

