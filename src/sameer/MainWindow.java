package sam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainWindow extends JFrame {
    private JTextArea sqlCommandArea;
    private String databaseName;

    public MainWindow(String databaseName) {
        this.databaseName = databaseName;

        setTitle("Create Table");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sqlCommandArea = new JTextArea();
        add(new JScrollPane(sqlCommandArea), BorderLayout.CENTER);

        JButton createTableButton = new JButton("Create Table");
        createTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sqlCommand = sqlCommandArea.getText();
                System.out.println("SQL Command: " + sqlCommand);  // Debug print
                try {
                    executeSqlCommand(sqlCommand);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Error executing SQL command: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        add(createTableButton, BorderLayout.SOUTH);
    }

    private void executeSqlCommand(String sqlCommand) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        String user = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sqlCommand);
            JOptionPane.showMessageDialog(this, "Table created successfully!");

            String tableName = getTableName(sqlCommand);
            if (tableName != null) {
                SwingUtilities.invokeLater(() -> {
                    CRUDWindow crudWindow = new CRUDWindow(databaseName, tableName);
                    crudWindow.setVisible(true);
                    this.dispose(); // Close the current window
                });
            }
        }
    }

    private String getTableName(String sqlCommand) {
        String[] parts = sqlCommand.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("table") && i + 1 < parts.length) {
                return parts[i + 1];
            }
        }
        return null;
    }
}
