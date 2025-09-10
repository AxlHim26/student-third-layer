package org.example.DataLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectDB {
    private static Connection conn;
    private static volatile boolean schemaInitialized = false;

    public static Connection getConnection() throws Exception {
        if (conn == null || conn.isClosed()) {
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USERNAME");
            String pass = System.getenv("DB_PASSWORD");

            if (url == null) url = "jdbc:postgresql://localhost:5432/student_db";
            if (user == null) user = "student_user";
            if (pass == null) pass = "student_pass";

            System.out.println("Connecting to DB: " + url + " user=" + user);
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(true);
            ensureSchema(conn);
        }
        return conn;
    }

    private static synchronized void ensureSchema(Connection connection) {
        if (schemaInitialized) return;
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS student (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  name VARCHAR(255) NOT NULL,\n" +
                    "  age INT NOT NULL\n" +
                    ")");
            schemaInitialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to ensure schema", e);
        }
    }
}
