package org.example.WindowUI;

import org.example.WindowUI.Ui.StudentUI;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentUI ui = new StudentUI();
            ui.setVisible(true);
        });
    }
}
