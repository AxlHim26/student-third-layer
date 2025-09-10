package org.example.WindowUI.Ui;

import org.example.BusLayer.Model.Student;
import org.example.WindowUI.SocketConnect.SocketConnect;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class StudentUI extends JFrame {
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Tên", "Tuổi"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final SocketConnect connect = new SocketConnect();
    private final List<Student> current = new ArrayList<>();

    public StudentUI() {
        setTitle("Quản lý Sinh viên");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton btnAdd = new JButton("Thêm");
        JButton btnDelete = new JButton("Xoá");

        btnAdd.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 1, 150, 1));
            JPanel form = new JPanel(new GridLayout(0, 1));
            form.add(new JLabel("Tên"));
            form.add(nameField);
            form.add(new JLabel("Tuổi"));
            form.add(ageSpinner);
            int res = JOptionPane.showConfirmDialog(this, form, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                int age = (Integer) ageSpinner.getValue();
                try {
                    connect.addStudent(new Student(0, name, age));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && row < current.size()) {
                int id = current.get(row).getId();
                int confirm = JOptionPane.showConfirmDialog(this, "Xoá sinh viên ID=" + id + "?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
                if (confirm == JOptionPane.OK_OPTION) {
                    try {
                        connect.deleteStudent(id);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(btnAdd);
        panel.add(btnDelete);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        connect.onStudentsUpdated(this::renderStudents);
        connect.startSubscription();
        initialLoad();
    }

    private void initialLoad() {
        try {
            List<Student> students = connect.getAllStudents();
            renderStudents(students);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderStudents(List<Student> students) {
        SwingUtilities.invokeLater(() -> {
            current.clear();
            current.addAll(students);
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAge()});
            }
        });
    }
}
