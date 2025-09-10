package org.example.BusLayer.Handle;

import org.example.BusLayer.Model.Student;
import org.example.DataLayer.DAO;

import java.util.List;

public class StudentBUS {
    private DAO dao;

    public StudentBUS() {
        this.dao = new DAO();
    }

    public List<Student> getAllStudents() throws Exception {
        return dao.getAll();
    }

    public void addStudent(Student s) throws Exception {
        if (s.getName() == null || s.getName().isEmpty()) throw new Exception("ten khong duoc rong");
        if (s.getAge() <= 0) throw new Exception("tuoi phai > 0");
        dao.insert(s);
    }

    public void updateStudent(Student s) throws Exception {
        dao.update(s);
    }

    public void deleteStudent(int id) throws Exception {
        dao.delete(id);
    }
}
