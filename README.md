Student Management App

A simple Java Client–Server app with PostgreSQL database.
Client (Swing UI) connects to Server (Socket) → Server saves/loads data from DB.

Prerequisites

Java 21

Maven

Docker (with Colima on Mac/Linux or Docker Desktop on Windows)

Run Database
colima start          # (Mac/Linux)
docker-compose up --build


Then create table:

docker exec -it student_postgres psql -U student_user -d student_db

CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

Run App
Start server
mvn clean package -DskipTests
java -cp target/client-server-3layer-1.0-SNAPSHOT.jar org.example.BusLayer.Server.ServerApp

Start client
java -cp target/client-server-3layer-1.0-SNAPSHOT.jar org.example.WindowUI.Ui.StudentUI

Usage

Add → enter student info → saved in DB

Load → show all students

Delete → remove selected student
