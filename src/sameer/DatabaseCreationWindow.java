package sam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreationWindow extends JFrame {
    private JTextField databaseNameField;

    public DatabaseCreationWindow() {
        setTitle("Create Database");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Enter Database Name:"));
        databaseNameField = new JTextField();
        panel.add(databaseNameField);
        add(panel, BorderLayout.CENTER);

        JButton createDatabaseButton = new JButton("Create Database");
        createDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String databaseName = databaseNameField.getText();
                System.out.println("Database Name: " + databaseName);  // Debug print
                try {
                    createDatabase(databaseName);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DatabaseCreationWindow.this, "Error creating database: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        add(createDatabaseButton, BorderLayout.SOUTH);
    }

    private void createDatabase(String databaseName) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE " + databaseName);
            JOptionPane.showMessageDialog(this, "Database created successfully!");

            SwingUtilities.invokeLater(() -> {
                new MainWindow(databaseName).setVisible(true);
                this.dispose(); // Close the current window
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DatabaseCreationWindow().setVisible(true);
        });
    }
}
