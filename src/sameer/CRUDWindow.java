package sam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDWindow extends JFrame {
    private String databaseName;
    private String tableName;
    private List<JTextField> inputFields;
    private JPanel inputPanel;

    public CRUDWindow(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;

        setTitle("CRUD Operations for " + tableName);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2));
        add(inputPanel, BorderLayout.CENTER);

        fetchTableStructure();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create");
        JButton retrieveButton = new JButton("Retrieve");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(createButton);
        buttonPanel.add(retrieveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        JButton openPhpMyAdminButton = new JButton("Open phpMyAdmin");
        openPhpMyAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI("http://localhost/phpmyadmin"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CRUDWindow.this, "Error opening phpMyAdmin: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Want to see changes? Click this button:"), BorderLayout.NORTH);
        bottomPanel.add(openPhpMyAdminButton, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.NORTH);
    }

    private void fetchTableStructure() {
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        String user = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery("DESCRIBE " + tableName);
            inputFields = new ArrayList<>();

            while (rs.next()) {
                String columnName = rs.getString("Field");
                JLabel label = new JLabel(columnName);
                JTextField textField = new JTextField();

                inputPanel.add(label);
                inputPanel.add(textField);
                inputFields.add(textField);
            }

            inputPanel.revalidate();
            inputPanel.repaint();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching table structure: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

