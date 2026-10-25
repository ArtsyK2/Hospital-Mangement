# Hospital Management System (Java Standalone)

A desktop application built with Java Swing. It does not require a database or third-party libraries.

## Features
- Add and delete patient records
- Add and delete doctor records
- Book and cancel appointments
- View records in separate tabs
- Automatically saves data locally in `hospital-data.ser`
- Deleting a patient or doctor also removes their appointments

## Requirements
- JDK 17 or newer
- A desktop environment capable of displaying Swing windows

## Run on Linux / macOS
From the project root:

```bash
javac -d out src/main/java/hms/*.java
java -cp out hms.HospitalManagementSystem
```

## Run on Windows
From the project root in Command Prompt:

```bat
if not exist out mkdir out
javac -d out src\main\java\hms\*.java
java -cp out hms.HospitalManagementSystem
```

The `hospital-data.ser` file is created in the directory from which the application is launched. Keep it to retain data; remove it only if you want to reset the application.

## Project structure

```text
HospitalManagementSystem/
├── README.md
└── src/
    └── main/
        └── java/
            └── hms/
                ├── Appointment.java
                ├── Doctor.java
                ├── HospitalManagementSystem.java
                └── Patient.java
```

## Notes
This is an educational/demo project. It stores data locally and does not implement authentication, encryption, multi-user access, or production-grade medical data protection. Do not use it for real patient records.
