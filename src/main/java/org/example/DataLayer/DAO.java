package org.example.DataLayer;

import org.example.BusLayer.Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, age FROM student";

        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("DB getAll failed", e);
        }

        return list;
    }

    public void insert(Student s) {
        String sql = "INSERT INTO student(name, age) VALUES (?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("DB insert failed", e);
        }
    }

    public void update(Student s) {
        String sql = "UPDATE student SET name=?, age=? WHERE id=?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setInt(3, s.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("DB update failed", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM student WHERE id=?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("DB delete failed", e);
        }
    }
}
