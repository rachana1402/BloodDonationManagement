

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionPanel extends JPanel {

    private JLabel transactionLabel;
    private JLabel bloodRequestLabel;
    private JComboBox<String> bloodRequestComboBox;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JButton processTransactionButton;
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private Connection conn;
    private int loggedInUserId;

    public TransactionPanel(Connection dbConn, int userId) {
        this.conn = dbConn;
        this.loggedInUserId = userId;
        initializeUI();
        populateBloodRequestComboBox();
        populateTransactionTable();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        transactionLabel = new JLabel("Blood Bank Transactions (Online Only)");
        transactionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(transactionLabel, gbc);

        bloodRequestLabel = new JLabel("Blood Request:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(bloodRequestLabel, gbc);

        bloodRequestComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(bloodRequestComboBox, gbc);

        amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(amountLabel, gbc);

        amountTextField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(amountTextField, gbc);

        processTransactionButton = new JButton("Process Online Transaction");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(processTransactionButton, gbc);

        // Transaction Table
        transactionTableModel = new DefaultTableModel(new Object[]{"Transaction ID", "Blood Request ID", "Amount", "Status"}, 0);
        transactionTable = new JTable(transactionTableModel);
        JScrollPane tableScrollPane = new JScrollPane(transactionTable);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(tableScrollPane, gbc);

        processTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTransaction();
            }
        });
    }

    private void populateBloodRequestComboBox() {
        bloodRequestComboBox.removeAllItems();
        try {
            String sql = "SELECT BR_id FROM BloodRequest WHERE status = 'Approved' OR status = 'Pending'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bloodRequestComboBox.addItem(rs.getString("BR_id"));
            }
             if (bloodRequestComboBox.getItemCount() == 0)
            {
               JOptionPane.showMessageDialog(this, "No Approved/Pending Blood Requests are available");
               processTransactionButton.setEnabled(false);
            }
            else
            {
                processTransactionButton.setEnabled(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching blood requests: " + ex.getMessage());
        }
    }

    private void populateTransactionTable() {
        transactionTableModel.setRowCount(0);
        try {
            String sql = "SELECT T_id, BR_id, amount, status FROM Transaction";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactionTableModel.addRow(new Object[]{
                        rs.getInt("T_id"),
                        rs.getInt("BR_id"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching transactions: " + ex.getMessage());
        }
    }

    private void processTransaction() {
        if (bloodRequestComboBox.getSelectedItem() == null || amountTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a blood request and enter the amount.");
            return;
        }

        String brId = bloodRequestComboBox.getSelectedItem().toString();
        String amountText = amountTextField.getText().trim();

        try {
            double amount = Double.parseDouble(amountText);

            String sql = "INSERT INTO Transaction (BR_id, mode, amount, status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(brId));
            pstmt.setString(2, "Online"); // Hardcoded to "Online"
            pstmt.setDouble(3, amount);
            pstmt.setString(4, "Completed");
            pstmt.executeUpdate();

             String updateBloodRequestSQL = "UPDATE BloodRequest SET status = 'Completed' WHERE BR_id = ?";
            PreparedStatement updateRequestStmt = conn.prepareStatement(updateBloodRequestSQL);
            updateRequestStmt.setInt(1,Integer.parseInt(brId));
            updateRequestStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Online transaction processed successfully!");
            populateTransactionTable();
            populateBloodRequestComboBox();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing transaction: " + ex.getMessage());
        }
    }

    private void clearFields() {
        bloodRequestComboBox.setSelectedIndex(0);
        amountTextField.setText("");
    }
}

