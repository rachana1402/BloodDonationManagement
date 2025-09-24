


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

import javax.swing.*; 
import javax.imageio.ImageIO;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*; 

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.*; 

import java.time.LocalDate; 

import java.time.LocalDateTime; 

import java.time.Period; 

import java.time.format.DateTimeFormatter;

// Login components 
public class BloodDonationApp extends JFrame { 
    CardLayout cardLayout; 

    JPanel mainPanel; 

    JLabel welcomeLabel;

    JTextField loginUsername; 

    JPasswordField loginPassword; 

 

    // Donor registration components 

    JTextField fnameField, lnameField, addressField, dobField, donatedDateField, phoneField, emailField, ageField,HospitalNameField,HospitalLocationField,HospitalEmailField,HospitalContactnoField; 
    

    JComboBox<String> bloodTypeBox; 

JTextField BloodBankNameField,BloodBankLocationField,BloodBankEmailField,BloodBankContactnoField; 



private int loginId; 

private int loggedInUserId; 

private JComboBox<String> hospitalNameBox; 

private JComboBox<String> bloodBankNameBox; 

private JTextField bloodBankAddressField; 

private JTextField bloodBankEmailField; 

private JPasswordField bloodBankPasswordField;


private JTextField bloodBankContactNoField; 
// private JPanel bloodRequestManagementPanel;
 

  

 

    public BloodDonationApp() { 

        setTitle("Vital Connect"); 

        setSize(450, 500); 

        setDefaultCloseOperation(EXIT_ON_CLOSE); 

        setLocationRelativeTo(null); 

 

        cardLayout = new CardLayout(); 

        mainPanel = new JPanel(cardLayout); 

 

        // A login and registration panels 

        mainPanel.add(createLoginPanel(), "Login"); 

        mainPanel.add(createDonorRegistrationPanel(), "Register as DONOR"); 

        mainPanel.add(createDonorDashboard(), "DonorDashboard"); 

         

         

        mainPanel.add(createHospitalRegistrationPanel(),"Register as HOSPITAL"); 

         

         

        mainPanel.add(createBloodBankRegistrationPanel(),"Register as BLOOD BANK"); 
        add(mainPanel); 
        cardLayout.show(mainPanel, "Login"); 

        setVisible(true); 

    } 

    private JPanel createLoginPanel() { 

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); 

 

        loginUsername = new JTextField(); 

        loginPassword = new JPasswordField(); 

 

        panel.add(new JLabel("Username:")); 

        panel.add(loginUsername); 

        panel.add(new JLabel("Password:")); 

        panel.add(loginPassword); 

 

        JButton loginBtn = new JButton("Login"); 

        JButton switchToRegister = new JButton("Register as Donor"); 

        JButton switchToHospitalRegister = new JButton("Register as Hospital"); 

        JButton switchToBloodBankRegister = new JButton("Register as Blood Bank"); 

         

        loginBtn.addActionListener(e -> handleLogin()); 

        switchToRegister.addActionListener(e -> cardLayout.show(mainPanel, "Register as DONOR")); 

        switchToHospitalRegister.addActionListener(e-> cardLayout.show(mainPanel,"Register as HOSPITAL")); 

        switchToBloodBankRegister.addActionListener(e-> cardLayout.show(mainPanel, "Register as BLOOD BANK")); 

        panel.add(switchToHospitalRegister); 

        panel.add(loginBtn); 

        panel.add(switchToRegister); 

        panel.add(switchToBloodBankRegister); 

 

        return panel; 

    } 

    private JPanel createDonorRegistrationPanel() {
        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));

        fnameField = addField(panel, "First Name:");
        lnameField = addField(panel, "Last Name:");
        addressField = addField(panel, "Address:");

        dobField = addField(panel, "DOB (YYYY-MM-DD):");
        dobField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    LocalDate dob = LocalDate.parse(dobField.getText());
                    LocalDate now = LocalDate.now();
                    int age = Period.between(dob, now).getYears();
                    ageField.setText(String.valueOf(age));
                } catch (Exception ex) {
                    ageField.setText("");
                    JOptionPane.showMessageDialog(null, "Invalid DOB format. Use YYYY-MM-DD");
                }
            }
        });

        donatedDateField = addField(panel, "Last Donated (YYYY-MM-DD):");

        panel.add(new JLabel("Blood Type:"));
        bloodTypeBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        panel.add(bloodTypeBox);

        phoneField = addField(panel, "Phone Number:");
        emailField = addField(panel, "Email:");
        ageField = addField(panel, "Age:");
        ageField.setEditable(false);

        JButton registerBtn = new JButton("Register");
        JButton backToLogin = new JButton("Back to Login");

        registerBtn.addActionListener(e -> {
            if (!validateInputs()) return;
            registerDonor();
        });

        backToLogin.addActionListener(e -> {
            clearFields();
            cardLayout.show(mainPanel, "Login");
        });

        panel.add(registerBtn);
        panel.add(backToLogin);

        return panel;
    }

    // Clear all fields
    private void clearFields() {
        fnameField.setText("");
        lnameField.setText("");
        addressField.setText("");
        dobField.setText("");
        donatedDateField.setText("");
        phoneField.setText("");
        emailField.setText("");
        ageField.setText("");
        bloodTypeBox.setSelectedIndex(0);
    }

    // Input validation method
    private boolean validateInputs() {
    	
    	try {
            LocalDate dob = LocalDate.parse(dobField.getText().trim());
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18) {
                JOptionPane.showMessageDialog(null, "Donor must be at least 18 years old to donate.");
                return false;
            }
            if(age > 65) {
                JOptionPane.showMessageDialog(null, "Donor must be less than 65 years old to donate.");
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid DOB format. Use YYYY-MM-DD");
            return false;
        }
    	
    	String firstname = fnameField.getText().trim();
    	if(!firstname.matches("[a-zA-Z]+")) {
    		JOptionPane.showMessageDialog(null, "Enter valid first name");
            return false;
    	}
    	
    	String lastname = fnameField.getText().trim();
    	if(!lastname.matches("[a-zA-Z]+")) {
    		JOptionPane.showMessageDialog(null, "Enter valid last name");
            return false;
    	}

        String address = addressField.getText().trim();
        if (!address.matches("[a-zA-Z0-9 #/,]+")) {
            JOptionPane.showMessageDialog(null, "Address contains invalid characters.");
            return false;
        }
    	
        String phone = phoneField.getText().trim();
        if (!phone.matches("\\d{10}") || phone.equals("0000000000") || phone.equals("1111111111")|| phone.equals("2222222222")) {
            JOptionPane.showMessageDialog(null, "Invalid phone number.");
            return false;
        }

        // Email validation
        String email = emailField.getText().trim();
        if (!email.endsWith("@gmail.com") || !email.matches("^[a-zA-Z0-9 _.]+@gmail\\.com$")) {
            JOptionPane.showMessageDialog(null, "Enter the proper email id");
            return false;
        }

        // Last Donated Date validation (must be 18+ at that time)
        String donatedText = donatedDateField.getText().trim();
        if (!donatedText.isEmpty()) {
            try {
                LocalDate donatedDate = LocalDate.parse(donatedText);
                LocalDate dob = LocalDate.parse(dobField.getText());
                int ageAtDonation = Period.between(dob, donatedDate).getYears();
                if (ageAtDonation < 18) {
                    JOptionPane.showMessageDialog(null, "Donor must be at least 18 years old at the time of last donation.");
                    return false;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid Last Donated date format. Use YYYY-MM-DD");
                return false;
            }
        }

        return true;
    }

    private JPanel createDonorDashboard() { 

        JPanel panel = new JPanel(new BorderLayout()); 
        welcomeLabel = new JLabel("Welcome ", JLabel.CENTER); 
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(welcomeLabel, BorderLayout.NORTH); 
        JPanel buttonPanel = new JPanel(new GridLayout(6,1,10,10)); 

        JButton profileBtn = new JButton("View Profile"); 

        JButton bookApptBtn = new JButton("Book Appointment"); 

        JButton apptHistoryBtn = new JButton("Appointment History"); 

        JButton donationHistoryBtn = new JButton("Donation History"); 

        JButton notificationBtn = new JButton("Notifications"); 

        JButton viewreportBtn = new JButton("View My Report"); 

        JButton logoutBtn = new JButton("Logout"); 
        profileBtn.addActionListener(e -> cardLayout.show(mainPanel, "DonorProfile")); 

        bookApptBtn.addActionListener(e -> cardLayout.show(mainPanel, "BookAppointment")); 

        apptHistoryBtn.addActionListener(e -> cardLayout.show(mainPanel, "AppointmentHistory")); 

        donationHistoryBtn.addActionListener(e -> cardLayout.show(mainPanel, "DonationHistory")); 

        notificationBtn.addActionListener(e -> cardLayout.show(mainPanel, "Notifications")); 

        viewreportBtn.addActionListener(e-> cardLayout.show(mainPanel,"ViewReport")); 

        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login")); 

        buttonPanel.add(profileBtn); 

        buttonPanel.add(bookApptBtn); 

        buttonPanel.add(apptHistoryBtn); 

        buttonPanel.add(donationHistoryBtn); 

        buttonPanel.add(notificationBtn); 

        buttonPanel.add(viewreportBtn); 

        buttonPanel.add(logoutBtn); 

        panel.add(buttonPanel, BorderLayout.CENTER); 

        return panel; 

    } 



    private JPanel createDonorProfilePanel(int loginId) { 

        JPanel panel = new JPanel(new BorderLayout()); 

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); 

        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); 

        JLabel nameLabel = new JLabel("Name:"); 

        JLabel addressLabel = new JLabel("Address:"); 

        JLabel dobLabel = new JLabel("Date of Birth:"); 

        JLabel donatedDateLabel = new JLabel("Last Donated Date:"); 

        JLabel bloodTypeLabel = new JLabel("Blood Type:"); 

        JLabel phoneLabel = new JLabel("Phone:"); 

        JLabel emailLabel = new JLabel("Email:"); 

        JLabel ageLabel = new JLabel("Age:"); 

        JLabel nameValue = new JLabel(); 

        JLabel addressValue = new JLabel(); 

        JLabel dobValue = new JLabel(); 

        JLabel donatedDateValue = new JLabel(); 

        JLabel bloodTypeValue = new JLabel(); 

        JLabel phoneValue = new JLabel(); 

        JLabel emailValue = new JLabel(); 

        JLabel ageValue = new JLabel(); 


        formPanel.add(nameLabel); formPanel.add(nameValue); 

        formPanel.add(addressLabel); formPanel.add(addressValue); 

        formPanel.add(dobLabel); formPanel.add(dobValue); 

        formPanel.add(donatedDateLabel); formPanel.add(donatedDateValue); 

        formPanel.add(bloodTypeLabel); formPanel.add(bloodTypeValue); 

        formPanel.add(phoneLabel); formPanel.add(phoneValue); 

        formPanel.add(emailLabel); formPanel.add(emailValue); 

        formPanel.add(ageLabel); formPanel.add(ageValue); 


        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            String query = "SELECT * FROM donor WHERE login_id = ?"; 

            PreparedStatement stmt = conn.prepareStatement(query); 

            stmt.setInt(1, loginId); 

            ResultSet rs = stmt.executeQuery(); 

            if (rs.next()) { 

                String fullName = rs.getString("first_name") + " " + rs.getString("last_name"); 

                nameValue.setText(fullName); 

                addressValue.setText(rs.getString("address")); 

                dobValue.setText(rs.getString("dob")); 

                donatedDateValue.setText(rs.getString("donated_date")); 

                bloodTypeValue.setText(rs.getString("bloodtype")); 

                phoneValue.setText(rs.getString("ph_no")); 

                emailValue.setText(rs.getString("email")); 

                ageValue.setText(String.valueOf(rs.getInt("age"))); 

            } else { 

                JOptionPane.showMessageDialog(panel, "No profile data found for this user."); 

            } 

        } catch (Exception e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(panel, "Error fetching donor profile data."); 

        } 


        panel.add(new JLabel("Donor Profile", JLabel.CENTER), BorderLayout.NORTH); 

        panel.add(formPanel, BorderLayout.CENTER); 

        JButton backButton = new JButton("Back to Dashboard"); 

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DonorDashboard")); 

        JPanel buttonPanel = new JPanel(); 

        buttonPanel.add(backButton); 

        panel.add(buttonPanel, BorderLayout.SOUTH); 

 

        return panel; 

    } 
    private JPanel createAppointmentBookingPanel(int donorId) { 
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
    
        JLabel titleLabel = new JLabel("Book Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, gbc);
    
        // Fetch blood bank names and IDs from the database
        Vector<String> bloodBanks = new Vector<>();
        Vector<Integer> bloodBankIds = new Vector<>();
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            String query = "SELECT BB_id, name FROM BloodBank";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                bloodBankIds.add(rs.getInt("BB_id"));
                bloodBanks.add(rs.getString("name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
        // Blood Bank dropdown
        gbc.gridy++;
        panel.add(new JLabel("Select Blood Bank:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> bbDropdown = new JComboBox<>(bloodBanks);
        panel.add(bbDropdown, gbc);
    
        // Date & Time input
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Date & Time (YYYY-MM-DD HH:MM:SS):"), gbc);
    
        // Ensure the datetimeField gets enough space to be visible
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Ensure the field expands horizontally
        gbc.weightx = 1.0;  // Allow the text field to take up more space
        JTextField datetimeField = new JTextField(15);
        panel.add(datetimeField, gbc);
    
        // Book Appointment button
        gbc.gridy++;
        JButton bookBtn = new JButton("Book Appointment");
        panel.add(bookBtn, gbc);
    
        // Status label
        gbc.gridy++;
        JLabel statusLabel = new JLabel("");
        panel.add(statusLabel, gbc);
    
        // Button action listener
        bookBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
                    int selectedBBId = bloodBankIds.get(bbDropdown.getSelectedIndex());
                    String dateTimeStr = datetimeField.getText().trim();
    
                    if (dateTimeStr.isEmpty()) {
                        statusLabel.setText("Please enter a valid date and time.");
                        return;
                    }
    
                    LocalDate appointmentDate = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
    
                    // Optional: Prevent booking in the past
                    if (appointmentDate.isBefore(LocalDate.now())) {
                        statusLabel.setText("Appointment date cannot be in the past.");
                        return;
                    }
    
                    LocalDate lastDonationDate = null;
    
                    // Step 1: Try to get latest appointment date
                    String latestApptQuery = "SELECT MAX(date) AS latest_date FROM Appointment WHERE D_id = ?";
                    PreparedStatement apptStmt = conn.prepareStatement(latestApptQuery);
                    apptStmt.setInt(1, donorId);
                    ResultSet apptRS = apptStmt.executeQuery();
    
                    if (apptRS.next()) {
                        Timestamp latestApptTS = apptRS.getTimestamp("latest_date");
                        if (latestApptTS != null) {
                            lastDonationDate = latestApptTS.toLocalDateTime().toLocalDate();
                        }
                    }
    
                    // Step 2: If no appointments found, check donated_date from donor table
                    if (lastDonationDate == null) {
                        String donatedDateQuery = "SELECT donated_date FROM donor WHERE D_id = ?";
                        PreparedStatement donatedStmt = conn.prepareStatement(donatedDateQuery);
                        donatedStmt.setInt(1, donorId);
                        ResultSet donatedRS = donatedStmt.executeQuery();
    
                        if (donatedRS.next()) {
                            Date donatedDateSql = donatedRS.getDate("donated_date");
                            if (donatedDateSql != null) {
                                lastDonationDate = donatedDateSql.toLocalDate();
                            }
                        }
                    }
    
                    // Step 3: If we found any previous date, enforce 56-day rule
                    if (lastDonationDate != null) {
                        LocalDate nextEligibleDate = lastDonationDate.plusDays(56);
                        if (appointmentDate.isBefore(nextEligibleDate)) {
                            statusLabel.setText("You must wait 56 days between donations.\nNext eligible date: " + nextEligibleDate);
                            return;
                        }
                    }
    
                    // Step 4: Book the appointment
                    String insertSQL = "INSERT INTO Appointment (date, D_id, BB_id) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(insertSQL);
                    stmt.setString(1, dateTimeStr);
                    stmt.setInt(2, donorId);
                    stmt.setInt(3, selectedBBId);
    
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        statusLabel.setText("Appointment booked successfully!");
                    } else {
                        statusLabel.setText("Failed to book appointment.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }
        });
    
        gbc.gridy++;
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DonorDashboard"));
        panel.add(backButton, gbc);
    
        return panel;
    }
    

    private JPanel createDonationHistoryPanel(int donorId) { 
        JPanel panel = new JPanel(new BorderLayout()); 
        JPanel historyPanel = new JPanel(); 
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS)); 
    
        // Label
        JLabel titleLabel = new JLabel("Donation History"); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(titleLabel, BorderLayout.NORTH); 
    
        // Fetch donation history from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 
            String query = "SELECT donation_date, bloodtype, BloodDonation_id FROM blooddonation WHERE D_id = ?"; 
            PreparedStatement stmt = conn.prepareStatement(query); 
            stmt.setInt(1, donorId);  // Use the donorId passed as argument
    
            ResultSet rs = stmt.executeQuery(); 
    
            // Check if there are any results and display them
            while (rs.next()) { 
                String donationDate = rs.getString("donation_date"); 
                String bloodType = rs.getString("bloodtype"); 
                String donationId = rs.getString("BloodDonation_id"); 
    
                // Print to debug
                System.out.println("Donation ID: " + donationId);
                System.out.println("Donation Date: " + donationDate);
                System.out.println("Blood Type: " + bloodType);
    
                // Create a label for each donation and add it to the panel
                String donationInfo = "Donation ID: " + donationId + " | Date: " + donationDate + " | Blood Type: " + bloodType; 
                JLabel donationLabel = new JLabel(donationInfo); 
                historyPanel.add(donationLabel); 
            } 
    
            // If no donations are found
            if (historyPanel.getComponentCount() == 0) { 
                historyPanel.add(new JLabel("No donation history found.")); 
            }
    
        } catch (SQLException e) { 
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(panel, "Error fetching donation history: " + e.getMessage()); 
        }
    
        // Add history panel to the main panel
        panel.add(new JScrollPane(historyPanel), BorderLayout.CENTER); 
    
        // Back button to go back to the donor dashboard
        JButton backButton = new JButton("Back to Dashboard"); 
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "DonorDashboard"); // Navigating back to DonorDashboard
        });
    
        JPanel buttonPanel = new JPanel(); 
        buttonPanel.add(backButton); 
        panel.add(buttonPanel, BorderLayout.SOUTH); 
    
        return panel; 
    }
    

    
     

    
    

    private JPanel createAppointmentHistoryPanel(int donorId) { 

        JPanel panel = new JPanel(new BorderLayout()); 

        DefaultTableModel model = new DefaultTableModel(); 

        JTable table = new JTable(model); 

 

        model.addColumn("Appointment ID"); 

        model.addColumn("Date & Time"); 

        model.addColumn("Blood Bank Name"); 

        model.addColumn("Blood Bank Location"); 

 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            String query = """ 

                SELECT a.Appointment_id, a.date, b.name AS bloodbank_name, b.location 

                FROM Appointment a 

                JOIN BloodBank b ON a.BB_id = b.BB_id 

                WHERE a.D_id = ? 

                ORDER BY a.date DESC 

            """; 

 

            PreparedStatement stmt = conn.prepareStatement(query); 

            stmt.setInt(1, donorId); 

            ResultSet rs = stmt.executeQuery(); 

 

            while (rs.next()) { 

                model.addRow(new Object[]{ 

                    rs.getInt("Appointment_id"), 

                    rs.getTimestamp("date"), 

                    rs.getString("bloodbank_name"), 

                    rs.getString("location") 

                }); 

            } 

        } catch (Exception e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(panel, "Error loading appointment history."); 

        } 

 

        panel.add(new JLabel("Your Appointment History", JLabel.CENTER), BorderLayout.NORTH); 

        panel.add(new JScrollPane(table), BorderLayout.CENTER); 

        JButton backButton = new JButton("Back to Dashboard"); 

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DonorDashboard")); 

        JPanel buttonPanel = new JPanel(); 

        buttonPanel.add(backButton); 

        panel.add(buttonPanel, BorderLayout.SOUTH); 

        return panel; 

    } 

 

    private JPanel createReceiveNotificationPanel(int donorId) { 

        JPanel panel = new JPanel(new BorderLayout()); 

        DefaultListModel<String> model = new DefaultListModel<>(); 

        JList<String> notificationList = new JList<>(model); 

        panel.add(new JLabel("Your Notifications", JLabel.CENTER), BorderLayout.NORTH); 

        panel.add(new JScrollPane(notificationList), BorderLayout.CENTER); 

        JButton backButton = new JButton("Back to Dashboard"); 

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DonorDashboard")); 

        JPanel buttonPanel = new JPanel(); 

        buttonPanel.add(backButton); 

        panel.add(buttonPanel, BorderLayout.SOUTH); 

 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            String query = "SELECT message, status, created_at FROM Notification WHERE D_id = ? ORDER BY created_at DESC"; 

            PreparedStatement stmt = conn.prepareStatement(query); 

            stmt.setInt(1, donorId); 

            ResultSet rs = stmt.executeQuery(); 

 

            while (rs.next()) { 

                String message = rs.getString("message"); 

                String status = rs.getString("status"); 

                Timestamp ts = rs.getTimestamp("created_at"); 

                model.addElement("[" + ts.toString() + "] (" + status + "): " + message); 

            } 

        } catch (Exception e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(panel, "Error loading notifications."); 

        } 

 

        return panel; 

    } 

    private JPanel createViewReportsPanel(int donorId) {
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel title = new JLabel("My Uploaded Blood Reports", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);
    
        // Table setup
        String[] columnNames = {"Report Name", "Action"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        Map<Integer, String> reportIdToNameMap = new HashMap<>();
        Map<Integer, byte[]> reportDataMap = new HashMap<>();
    
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
    
            String sql = "SELECT R_id, report_name, report_data FROM donorreports WHERE D_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, donorId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                int id = rs.getInt("R_id");
                String name = rs.getString("report_name");
                byte[] data = rs.getBytes("report_data");
    
                reportIdToNameMap.put(id, name);
                reportDataMap.put(id, data);
                tableModel.addRow(new Object[]{name, "View"});
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error loading reports from database.");
        }
    
        // Add "View" button functionality
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), row -> {
            String reportName = (String) tableModel.getValueAt(row, 0);
    
            int reportId = -1;
            for (Map.Entry<Integer, String> entry : reportIdToNameMap.entrySet()) {
                if (entry.getValue().equals(reportName)) {
                    reportId = entry.getKey();
                    break;
                }
            }
    
            if (reportId != -1) {
                try {
                    byte[] fileBytes = reportDataMap.get(reportId);
                    String extension = reportName.contains(".") ? reportName.substring(reportName.lastIndexOf(".")) : ".pdf";
                    File file = new File("TempReport_" + System.currentTimeMillis() + extension);
    
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(fileBytes);
                    }
    
                    Desktop.getDesktop().open(file);
    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Failed to open report.");
                }
            }
        }));
    
        // Back to Dashboard Button
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "DonorDashboard"));
        JPanel btnPanel = new JPanel();
        btnPanel.add(backBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
    
        return panel;
    }
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "View" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private int selectedRow;
    private Consumer<Integer> onClick;

    public ButtonEditor(JCheckBox checkBox, Consumer<Integer> onClick) {
        super(checkBox);
        this.onClick = onClick;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "View" : value.toString();
        button.setText(label);
        isPushed = true;
        selectedRow = row;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            onClick.accept(selectedRow);
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
    

private JPanel createHospitalRegistrationPanel() {
    JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

    String[] mysoreHospitals = {
        "JSS Hospital", "Apollo BGS", "K.R. Hospital", "Narayana Multispeciality",
        "chandrakala Hospital", "Manipal Hospital", "Jayadev Hospital", "Kamakshi Hospital"
    };

    hospitalNameBox = new JComboBox<>(mysoreHospitals);
    HospitalLocationField = new JTextField();
    HospitalEmailField = new JTextField();
    HospitalContactnoField = new JTextField();
    JPasswordField HospitalPasswordField = new JPasswordField();

    panel.add(new JLabel("Hospital Name:"));
    panel.add(hospitalNameBox);
    panel.add(new JLabel("Location:"));
    panel.add(HospitalLocationField);
    panel.add(new JLabel("Email :"));
    panel.add(HospitalEmailField);
    panel.add(new JLabel("Contact No :"));
    panel.add(HospitalContactnoField);
    panel.add(new JLabel("Set Password:"));
    panel.add(HospitalPasswordField);

    JButton registerBtn = new JButton("Register Hospital");
    JButton backBtn = new JButton("Back to Login");

    registerBtn.addActionListener(e -> {
        String hospitalName = hospitalNameBox.getSelectedItem().toString();
        String location = HospitalLocationField.getText().trim();
        String email = HospitalEmailField.getText().trim();
        String contact = HospitalContactnoField.getText().trim();
        String password = new String(HospitalPasswordField.getPassword()).trim();

        if (!hospitalName.matches("^[A-Za-z ]+$")) {
            JOptionPane.showMessageDialog(this, "Hospital name must contain only letters and spaces.");
            return;
        }

        if (location.length() < 5) {
            JOptionPane.showMessageDialog(this, "Location must be at least 5 characters.");
            return;
        }

        if (!email.matches("^[a-zA-Z0-9]+@gmail\\.com$")) {
            JOptionPane.showMessageDialog(this, "Email must be valid (only letters/numbers before @gmail.com)");
            return;
        }

        if (!contact.matches("\\d{10}") || contact.equals("0000000000") || contact.startsWith("0")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits, not start with 0, and not all zeros.");
            return;
        }

        if (password.length() < 6 || password.length() > 25) {
            JOptionPane.showMessageDialog(this, "Password must be between 6 and 25 characters.");
            return;
        }

        boolean hasUpper = false, hasLower = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if ("!@#$%^&*()-_+=<>?/[]{}|".indexOf(c) >= 0) hasSpecial = true;
        }

        if (!hasUpper || !hasLower || !hasSpecial) {
            StringBuilder msg = new StringBuilder("Password must contain:\n");
            if (!hasUpper) msg.append("- At least one uppercase letter (A-Z)\n");
            if (!hasLower) msg.append("- At least one lowercase letter (a-z)\n");
            if (!hasSpecial) msg.append("- At least one special character (!@#$%^&*()-_+=<>?/[]{}|)\n");
            JOptionPane.showMessageDialog(this, msg.toString());
            return;
        }

        hospitalregister(password);
    });

    backBtn.addActionListener(e -> {
        clearFields();
        cardLayout.show(mainPanel, "Login");
    });
            
    panel.add(registerBtn);
    panel.add(backBtn);

    return panel;
}
private void hospitalregister(String password) {
    String username = hospitalNameBox.getSelectedItem().toString().toLowerCase().replaceAll("\\s+", "");
    String location = HospitalLocationField.getText().trim();
    String email = HospitalEmailField.getText().trim();
    String contact = HospitalContactnoField.getText().trim();

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
        conn.setAutoCommit(false);

        // Check duplicate username
        String checkUser = "SELECT COUNT(*) FROM login WHERE username = ?";
        PreparedStatement userStmt = conn.prepareStatement(checkUser);
        userStmt.setString(1, username);
        ResultSet userRs = userStmt.executeQuery();
        if (userRs.next() && userRs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "This hospital is already registered.");
            return;
        }

        // Check duplicate email
        String checkEmail = "SELECT COUNT(*) FROM hospitalregister WHERE email = ?";
        PreparedStatement emailStmt = conn.prepareStatement(checkEmail);
        emailStmt.setString(1, email);
        ResultSet emailRs = emailStmt.executeQuery();
        if (emailRs.next() && emailRs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "This email is already registered.");
            return;
        }

        // Check duplicate phone
        String checkPhone = "SELECT COUNT(*) FROM hospitalregister WHERE contact_no = ?";
        PreparedStatement phoneStmt = conn.prepareStatement(checkPhone);
        phoneStmt.setString(1, contact);
        ResultSet phoneRs = phoneStmt.executeQuery();
        if (phoneRs.next() && phoneRs.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "This phone number is already registered.");
            return;
        }

        // Insert into login table
        String loginSQL = "INSERT INTO login(username, password, role) VALUES (?, ?, 'hospital')";
        PreparedStatement loginstmt = conn.prepareStatement(loginSQL, Statement.RETURN_GENERATED_KEYS);
        loginstmt.setString(1, username);
        loginstmt.setString(2, password);
        loginstmt.executeUpdate();

        ResultSet rs = loginstmt.getGeneratedKeys();
        int loginId = -1;
        if (rs.next()) loginId = rs.getInt(1);
        if (loginId == -1) throw new SQLException("Failed to retrieve login ID.");

        // Insert into hospitalregister table
        String hospitalSQL = "INSERT INTO hospitalregister (Hlogin_id, name, location, email, contact_no) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement hospStmt = conn.prepareStatement(hospitalSQL);
        hospStmt.setInt(1, loginId);
        hospStmt.setString(2, hospitalNameBox.getSelectedItem().toString());
        hospStmt.setString(3, location);
        hospStmt.setString(4, email);
        hospStmt.setString(5, contact);
        hospStmt.executeUpdate();

        conn.commit();
        JOptionPane.showMessageDialog(this, "Hospital Registered Successfully!\nUsername: " + username + "\nPassword: " + password);
        cardLayout.show(mainPanel, "Login");

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Hospital Registration Error: " + ex.getMessage());
    }
}

     

    private JPanel createHospitalRequestPanel() { 

    	JPanel panel = new JPanel(new GridLayout(9,2,10,10)); 

    	 

    	 

    	 HospitalNameField = new JTextField(); 

    	 HospitalLocationField = new JTextField(); 

    	HospitalEmailField = new JTextField(); 

    	 HospitalContactnoField = new JTextField(); 

    	 

    	panel.add(new JLabel("Hospital Name: ")); 

    	panel.add(HospitalNameField); 

    	panel.add(new JLabel("Hosiptal Location: ")); 

    	panel.add(HospitalLocationField); 

    	panel.add(new JLabel("Hospital Email:")); 

    	panel.add(HospitalEmailField); 

    	panel.add(new JLabel("Hosiptal 	Contact no: ")); 

    	panel.add(HospitalContactnoField); 

    	 

    	JButton registerBtn = new JButton("Register Hospital"); 

    	JButton backbtn = new JButton("Back to login"); 

    	 registerBtn.addActionListener(e -> hospitalregister()); 

         backbtn.addActionListener(e -> cardLayout.show(mainPanel, "Login")); 

 

    	 

    	panel.add(registerBtn); 

    	panel.add(backbtn); 

    	 

    	return panel; 

    } 

     

     

    private JPanel createHospitalDashboardPanel(int hospitalId) {
        JPanel panel = new JPanel(new BorderLayout());
    
        String hospitalName = "";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM hospital WHERE H_id = ?");
            stmt.setInt(1, hospitalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                hospitalName = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        JLabel titleLabel = new JLabel("Welcome " + hospitalName, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
    
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
    
        JButton profileBtn = new JButton("View Hospital Profile");
        JButton requestHistoryBtn = new JButton("View Blood Request History");
        JButton notificationBtn = new JButton("Notifications");
        JButton requestBloodBtn = new JButton("Request Blood");
        JButton logoutBtn = new JButton("Logout");
    
        profileBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalProfile"));
        requestHistoryBtn.addActionListener(e -> cardLayout.show(mainPanel, "viewbloodRequestHistory"));
        notificationBtn.addActionListener(e -> cardLayout.show(mainPanel, "Notifications"));
        requestBloodBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodRequest"));
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
    
        buttonPanel.add(profileBtn);
        buttonPanel.add(requestHistoryBtn);
        buttonPanel.add(notificationBtn);
        buttonPanel.add(requestBloodBtn);
        buttonPanel.add(logoutBtn);
    
        panel.add(buttonPanel, BorderLayout.CENTER);
    
        return panel;
    }
    

     

     

    private JPanel createHospitalProfilePanel(int hospitalId) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            // Use H_id to fetch hospital profile data
            String sql = "SELECT name, location, email, contact_no FROM hospitalregister WHERE H_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, hospitalId); // Set the passed hospitalId
    
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                // Display hospital data on the panel
                panel.add(new JLabel("Name:"));
                panel.add(new JLabel(rs.getString("name")));
                panel.add(new JLabel("Location:"));
                panel.add(new JLabel(rs.getString("location")));
                panel.add(new JLabel("Email:"));
                panel.add(new JLabel(rs.getString("email")));
                panel.add(new JLabel("Contact No:"));
                panel.add(new JLabel(rs.getString("contact_no")));
            } else {
                // If no hospital data is found, display this message
                panel.add(new JLabel("No hospital data found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hospital profile: " + e.getMessage());
        }
    
        // Back button to navigate to the Hospital Dashboard
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalDashboard"));
    
        panel.add(backBtn);
    
        return panel;
    }
    

    private JPanel createBloodRequestPanel(int hospitalId) {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
    
        JTextField patientIdField = new JTextField();
        JTextField patientNameField = new JTextField();
        JComboBox<String> bloodTypeBox = new JComboBox<>(new String[]{
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        });
        JTextField quantityField = new JTextField();
        JTextField requestDateField = new JTextField(LocalDate.now().toString());
        JComboBox<String> bloodBankBox = new JComboBox<>();
    
        // Load blood banks from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT BB_id, name FROM bloodbank");
            while (rs.next()) {
                int bbId = rs.getInt("BB_id");
                String bbName = rs.getString("name");
                bloodBankBox.addItem(bbId + " - " + bbName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error loading blood banks: " + e.getMessage());
        }
    
        panel.add(new JLabel("Patient ID:")); panel.add(patientIdField);
        panel.add(new JLabel("Patient Name:")); panel.add(patientNameField);
        panel.add(new JLabel("Blood Type:")); panel.add(bloodTypeBox);
        panel.add(new JLabel("Quantity:")); panel.add(quantityField);
        panel.add(new JLabel("Request Date (YYYY-MM-DD):")); panel.add(requestDateField);
        panel.add(new JLabel("Select Blood Bank:")); panel.add(bloodBankBox);
    
        JButton requestBtn = new JButton("Request Blood");
        JButton backBtn = new JButton("Back to Dashboard");
    
        requestBtn.addActionListener(e -> {
            String patientIdStr = patientIdField.getText().trim();
            String patientName = patientNameField.getText().trim();
            String bloodType = (String) bloodTypeBox.getSelectedItem();
            String quantityStr = quantityField.getText().trim();
            String requestDateStr = requestDateField.getText().trim();
            String selectedBank = (String) bloodBankBox.getSelectedItem();
    
            if (patientIdStr.isEmpty() || patientName.isEmpty() || requestDateStr.isEmpty()
                    || quantityStr.isEmpty() || selectedBank == null) {
                JOptionPane.showMessageDialog(panel, "Please fill all fields.");
                return;
            }
    
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
                int patientId = Integer.parseInt(patientIdStr);
                int quantity = Integer.parseInt(quantityStr);
                java.sql.Date requestDate = java.sql.Date.valueOf(requestDateStr);
                int bbId = Integer.parseInt(selectedBank.split(" - ")[0]);
    
                PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM hospitalrequest WHERE P_id = ?");
                checkStmt.setInt(1, patientId);
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(panel, "Patient ID already exists.");
                    return;
                }
    
                PreparedStatement insertReqStmt = conn.prepareStatement(
                        "INSERT INTO hospitalrequest (H_id, P_id, bloodtype, name, request_date, quantity) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                insertReqStmt.setInt(1, hospitalId);
                insertReqStmt.setInt(2, patientId);
                insertReqStmt.setString(3, bloodType);
                insertReqStmt.setString(4, patientName);
                insertReqStmt.setDate(5, requestDate);
                insertReqStmt.setInt(6, quantity);
                insertReqStmt.executeUpdate();
    
                ResultSet generatedKeys = insertReqStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int HreqId = generatedKeys.getInt(1);
                    double amount = quantity * 100.0;
    
                    JPanel confirmPanel = new JPanel(new GridLayout(3, 1));
                    confirmPanel.add(new JLabel("Request submitted successfully."));
                    confirmPanel.add(new JLabel("Click OK to proceed with payment of Rs. " + amount));
    
                    JButton okBtn = new JButton("OK");
                    JButton backToDashBtn = new JButton("Back to Dashboard");
                    JPanel buttonPanel = new JPanel(new FlowLayout());
                    buttonPanel.add(okBtn);
                    buttonPanel.add(backToDashBtn);
                    confirmPanel.add(buttonPanel);
    
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), "Confirm Payment", true);
                    dialog.setContentPane(confirmPanel);
                    dialog.pack();
                    dialog.setLocationRelativeTo(panel);
    
                    okBtn.addActionListener(okEvent -> {
                        try {
                            PreparedStatement checkStock = conn.prepareStatement(
                                    "SELECT quantity FROM BloodStock WHERE BB_id = ? AND bloodtype = ?");
                            checkStock.setInt(1, bbId);
                            checkStock.setString(2, bloodType);
                            ResultSet stockRs = checkStock.executeQuery();
    
                            if (stockRs.next() && stockRs.getInt("quantity") >= quantity) {
    
                                // Reduce blood stock by requested quantity
                                PreparedStatement updateStock = conn.prepareStatement(
                                        "UPDATE BloodStock SET quantity = quantity - ? WHERE BB_id = ? AND bloodtype = ?");
                                updateStock.setInt(1, quantity);
                                updateStock.setInt(2, bbId);
                                updateStock.setString(3, bloodType);
                                updateStock.executeUpdate();
    
                              
    
                                PreparedStatement bloodReqStmt = conn.prepareStatement(
                                        "INSERT INTO bloodrequest (quantity, bloodtype, request_date, status, H_id, BB_id, transaction_status) VALUES (?, ?, ?, ?, ?, ?, ?)");
                                bloodReqStmt.setInt(1, quantity);
                                bloodReqStmt.setString(2, bloodType);
                                bloodReqStmt.setDate(3, requestDate);
                                bloodReqStmt.setString(4, "Pending");
                                bloodReqStmt.setInt(5, hospitalId);
                                bloodReqStmt.setInt(6, bbId);
                                bloodReqStmt.setString(7, "Pending");
                                bloodReqStmt.executeUpdate();
    
                                String message = "Blood request for patient: " + patientName + ", " + quantity + " unit(s) of " + bloodType + " blood on " + requestDateStr;
                                PreparedStatement notifyStmt = conn.prepareStatement(
                                        "INSERT INTO BloodBankNotification (BB_id, message, status, Hreq_id) VALUES (?, ?, 'unread', ?)");
                                notifyStmt.setInt(1, bbId);
                                notifyStmt.setString(2, message);
                                notifyStmt.setInt(3, HreqId);
                                notifyStmt.executeUpdate();
    
                                JOptionPane.showMessageDialog(panel, "Payment successful. Request recorded and stock reduced by " + quantity + " unit(s).");
                                dialog.dispose();
    
                            } else {
                                JOptionPane.showMessageDialog(panel, "Insufficient stock at selected blood bank.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(panel, "Payment successful ");
                        }
                    });
    
                    backToDashBtn.addActionListener(backEvent -> {
                        dialog.dispose();
                        cardLayout.show(mainPanel, "HospitalDashboard");
                    });
    
                    dialog.setVisible(true);
    
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to retrieve request ID.");
                }
    
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });
    
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalDashboard"));
    
        panel.add(requestBtn);
        panel.add(backBtn);
    
        return panel;
    }
    
    


    private JPanel createHospitalRequestHistoryPanel(int hospitalId) {
        JPanel panel = new JPanel(new BorderLayout());
    
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
    
        model.addColumn("Request ID");
        model.addColumn("Patient Name");
        model.addColumn("Blood Type");
        model.addColumn("Date");
        model.addColumn("Quantity");
        model.addColumn("Status"); // Placeholder for status
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
    
            String query = "SELECT * FROM hospitalrequest WHERE H_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, hospitalId);
            ResultSet rs = stmt.executeQuery();
    
            // Check if no rows are returned
            boolean dataFound = false;
    
            while (rs.next()) {
                dataFound = true;
                String requestDate = rs.getDate("request_date") != null ? rs.getDate("request_date").toString() : "No Date";
    
                model.addRow(new Object[]{
                    rs.getInt("Hreq_id"),
                    rs.getString("name"),
                    rs.getString("bloodtype"),
                    requestDate,               // Handle null date
                    rs.getInt("quantity"),     // Display quantity
                    "Pending"                  // Placeholder status
                });
    
                System.out.println("Fetched row: " + rs.getInt("Hreq_id")); // Debug
            }
    
            if (!dataFound) {
                model.addRow(new Object[]{"No blood requests found", "", "", "", "", ""});
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error loading request history.");
        }
    
        panel.add(new JLabel("Blood Request History", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
    
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalDashboard"));
        panel.add(backBtn, BorderLayout.SOUTH);
    
        return panel;
    }
    


    private JPanel createHospitalNotificationPanel(int hospitalId) { 

        JPanel panel = new JPanel(new BorderLayout()); 

        DefaultListModel<String> model = new DefaultListModel<>(); 

        JList<String> list = new JList<>(model); 

 

        panel.add(new JLabel("Notifications from Blood Bank", JLabel.CENTER), BorderLayout.NORTH); 

        panel.add(new JScrollPane(list), BorderLayout.CENTER); 

 

        JButton backBtn = new JButton("Back"); 

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalDashboard")); 

        panel.add(backBtn, BorderLayout.SOUTH); 

 

        // Load notifications 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            String sql = "SELECT message, created_at FROM HospitalNotification WHERE H_id = ? ORDER BY created_at DESC"; 

            PreparedStatement stmt = conn.prepareStatement(sql); 

            stmt.setInt(1, hospitalId); 

            ResultSet rs = stmt.executeQuery(); 

 

            while (rs.next()) { 

                String msg = "[" + rs.getTimestamp("created_at") + "] " + rs.getString("message"); 

                model.addElement(msg); 

            } 

        } catch (Exception e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(panel, "Error loading notifications: " + e.getMessage()); 

        } 

 

        return panel; 

    } 


    public JPanel createBloodBankRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel titleLabel = new JLabel("Blood Bank Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
    
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Blood Bank Name:"), gbc);
        gbc.gridx = 1;
        BloodBankNameField = new JTextField(20);
        panel.add(BloodBankNameField, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        BloodBankLocationField = new JTextField(20);
        panel.add(BloodBankLocationField, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Email (@gmail.com only):"), gbc);
        gbc.gridx = 1;
        BloodBankEmailField = new JTextField(20);
        panel.add(BloodBankEmailField, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Contact No (10 digits):"), gbc);
        gbc.gridx = 1;
        BloodBankContactnoField = new JTextField(20);
        panel.add(BloodBankContactnoField, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        bloodBankPasswordField = new JPasswordField(20);
        panel.add(bloodBankPasswordField, gbc);
    
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JTextArea passwordRequirements = new JTextArea(
            "Password Requirements:\n" +
            "- At least 6 characters\n" +
            "- At least one uppercase letter (A-Z)\n" +
            "- At least one lowercase letter (a-z)\n" +
            "- At least one special character (!@#$%^&*()-_+=<>?/[]{}|)"
        );
        passwordRequirements.setEditable(false);
        passwordRequirements.setBackground(panel.getBackground());
        passwordRequirements.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(passwordRequirements, gbc);
    
        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton registerBtn = new JButton("Register Blood Bank");
        JButton backBtn = new JButton("Back to Login");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
    
        registerBtn.setToolTipText("Click to register with the provided information");
        backBtn.setToolTipText("Return to login screen");
    
        registerBtn.addActionListener(e -> {
            if (BloodBankNameField.getText().trim().isEmpty() || BloodBankLocationField.getText().trim().isEmpty()
                    || BloodBankEmailField.getText().trim().isEmpty() || BloodBankContactnoField.getText().trim().isEmpty()
                    || new String(bloodBankPasswordField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields are required.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            BloodBankRegister();
        });
    
        backBtn.addActionListener(e -> {
        clearFields();
        cardLayout.show(mainPanel, "Login");
    });
    
        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
    
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
    
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        return panel;
    }
    private void BloodBankRegister() {
        String username = BloodBankNameField.getText().trim();
        String password = new String(bloodBankPasswordField.getPassword());
        String email = BloodBankEmailField.getText().trim();
        String phone = BloodBankContactnoField.getText().trim();
        String location = BloodBankLocationField.getText().trim();
    
        // Name validation
        if (username.length() < 3 || !username.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Blood Bank name must be at least 3 characters and contain only letters/spaces.");
            return;
        }
    
        if (location.length() < 5) {
            JOptionPane.showMessageDialog(this, "Location should be at least 5 characters long.");
            return;
        }
    
        // Email: only letters/numbers before @gmail.com
        if (!email.matches("^[a-zA-Z0-9]+@gmail\\.com$")) {
            JOptionPane.showMessageDialog(this, "Email must contain only letters and numbers before @gmail.com");
            return;
        }
    
        // Phone: 10 digits, not all 0, not starting with 0
        if (!phone.matches("\\d{10}") || phone.equals("0000000000") || phone.startsWith("0")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits, not start with 0, or be all zeros");
            return;
        }
    
        // Password: min 6 characters, 1 upper, 1 lower, 1 special
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.");
            return;
        }
    
        boolean hasUpper = false, hasLower = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if ("!@#$%^&*()-_+=<>?/[]{}|".indexOf(c) != -1) hasSpecial = true;
        }
    
        StringBuilder errorMsg = new StringBuilder("Password must contain:\n");
        boolean hasError = false;
        if (!hasUpper) { errorMsg.append("- At least one uppercase letter\n"); hasError = true; }
        if (!hasLower) { errorMsg.append("- At least one lowercase letter\n"); hasError = true; }
        if (!hasSpecial) { errorMsg.append("- At least one special character\n"); hasError = true; }
    
        if (hasError) {
            JOptionPane.showMessageDialog(this, errorMsg.toString());
            return;
        }
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            conn.setAutoCommit(false);
    
            String checkUsernameSQL = "SELECT COUNT(*) FROM login WHERE username = ?";
            PreparedStatement checkUserStmt = conn.prepareStatement(checkUsernameSQL);
            checkUserStmt.setString(1, username.toLowerCase().replaceAll("\\s+", ""));
            ResultSet userRs = checkUserStmt.executeQuery();
            if (userRs.next() && userRs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "This Blood Bank name is already registered.");
                return;
            }
    
            String checkEmailSQL = "SELECT COUNT(*) FROM BloodBank WHERE email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSQL);
            checkEmailStmt.setString(1, email);
            ResultSet emailRs = checkEmailStmt.executeQuery();
            if (emailRs.next() && emailRs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "This email is already registered.");
                return;
            }
    
            String checkPhoneSQL = "SELECT COUNT(*) FROM BloodBank WHERE contact_no = ?";
            PreparedStatement checkPhoneStmt = conn.prepareStatement(checkPhoneSQL);
            checkPhoneStmt.setString(1, phone);
            ResultSet phoneRs = checkPhoneStmt.executeQuery();
            if (phoneRs.next() && phoneRs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "This phone number is already registered.");
                return;
            }
    
            String loginSQL = "INSERT INTO login(username, password, role) VALUES (?, ?, 'bloodbank')";
            PreparedStatement loginStmt = conn.prepareStatement(loginSQL, Statement.RETURN_GENERATED_KEYS);
            loginStmt.setString(1, username.toLowerCase().replaceAll("\\s+", ""));
            loginStmt.setString(2, password);
            loginStmt.executeUpdate();
    
            ResultSet keys = loginStmt.getGeneratedKeys();
            int loginId = -1;
            if (keys.next()) loginId = keys.getInt(1);
            if (loginId == -1) throw new SQLException("Failed to retrieve login ID.");
    
            String insertBB = "INSERT INTO BloodBank (BBlogin_id, name, location, contact_no, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement bbStmt = conn.prepareStatement(insertBB);
            bbStmt.setInt(1, loginId);
            bbStmt.setString(2, username);
            bbStmt.setString(3, location);
            bbStmt.setString(4, phone);
            bbStmt.setString(5, email);
            bbStmt.executeUpdate();
    
            conn.commit();
            JOptionPane.showMessageDialog(this, "Blood bank registered successfully!\nUsername: "
                    + username.toLowerCase().replaceAll("\\s+", "") + "\nPassword: " + password);
            cardLayout.show(mainPanel, "Login");
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Blood bank registration error: " + ex.getMessage());
        }
    }
    

    private JPanel createBloodBankDashboard(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome Blood Bank", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Button panel with options for various actions
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10)); // Changed to 8 rows
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Buttons
        JButton profileBtn = new JButton("View Profile");
        JButton appointmentsBtn = new JButton("Appointments");
        JButton bloodStockBtn = new JButton("Blood Stock");
        JButton sendNotificationsBtn = new JButton("Send Notifications");
        JButton receiveNotificationBtn = new JButton("Receive Notification");
        JButton donorList=new JButton("DonorList");
        JButton logoutBtn = new JButton("Logout");

        // Style the buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        profileBtn.setFont(buttonFont);
        appointmentsBtn.setFont(buttonFont);
        bloodStockBtn.setFont(buttonFont);
        sendNotificationsBtn.setFont(buttonFont);
        receiveNotificationBtn.setFont(buttonFont);
        donorList.setFont(buttonFont);
        logoutBtn.setFont(buttonFont);

        // Action listeners for each button
        profileBtn.addActionListener(e -> {
            System.out.println("Navigating to Blood Bank Profile");
            cardLayout.show(mainPanel, "BloodBankProfile");
        });
        buttonPanel.add(profileBtn);
        
        appointmentsBtn.addActionListener(e -> {
            System.out.println("Navigating to Blood Bank Appointment List");
            cardLayout.show(mainPanel, "BloodBankAppointmentList");
        });
        buttonPanel.add(appointmentsBtn);
        
        bloodStockBtn.addActionListener(e -> {
            System.out.println("Navigating to Blood Stock");
            cardLayout.show(mainPanel, "BloodStock");
        });
        buttonPanel.add(bloodStockBtn);

        // Send notification button: Show notification options panel
        sendNotificationsBtn.addActionListener(e -> {
            System.out.println("Navigating to Send Notifications");
            cardLayout.show(mainPanel, "BloodBankSendNotifications");
        });
        buttonPanel.add(sendNotificationsBtn);
        
        // Receive notification button: Show view notifications panel
        receiveNotificationBtn.addActionListener(e -> {
            System.out.println("Navigating to Blood Bank View Notifications");
            cardLayout.show(mainPanel, "BloodBankViewNotifications");
        });
        buttonPanel.add(receiveNotificationBtn);
        
       donorList.addActionListener(e -> new DonorListFrame(bloodBankId)); // New Action

        buttonPanel.add(donorList);
        
        // Logout button: Navigate back to login screen
        logoutBtn.addActionListener(e -> {
            System.out.println("Logging out");
            cardLayout.show(mainPanel, "Login");
        });
        buttonPanel.add(logoutBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }





public class DonorListFrame extends JFrame {
    private List<Integer> donorIds = new ArrayList<>();
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public DonorListFrame(int bloodBankId) {
        setTitle("Donors List");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search bar
        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        topPanel.add(new JLabel("Search by Name: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Name", "Blood Group", "Donate", "Upload Image", "View Image"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                Object donated = getValueAt(row, 2);
                if ("Donated".equals(donated)) {
                    comp.setBackground(new Color(198, 239, 206)); // Green
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadDonorData(bloodBankId, "");

        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer("Donate"));
        table.getColumnModel().getColumn(2).setCellEditor(new DonateButtonEditor(new JCheckBox(), bloodBankId));
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("Upload"));
        table.getColumnModel().getColumn(3).setCellEditor(new UploadButtonEditor(new JCheckBox()));
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("View"));
        table.getColumnModel().getColumn(4).setCellEditor(new ViewButtonEditor(new JCheckBox()));

        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            loadDonorData(bloodBankId, searchText);
        });

        setVisible(true);
    }

    private void loadDonorData(int bloodBankId, String searchName) {
        model.setRowCount(0);
        donorIds.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            String query = """
                SELECT DISTINCT CONCAT(d.first_name, ' ', d.last_name) AS full_name, d.bloodtype, d.D_id
                FROM donor d
                JOIN appointment a ON d.D_id = a.D_id
                WHERE a.BB_id = ? AND CONCAT(d.first_name, ' ', d.last_name) LIKE ?
                """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, bloodBankId);
            ps.setString(2, "%" + searchName + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("full_name");
                String bloodGroup = rs.getString("bloodtype");
                int donorId = rs.getInt("D_id");
                donorIds.add(donorId);
                model.addRow(new Object[]{name, bloodGroup, "Donate", "Upload Image", "View Image"});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching donor data: " + e.getMessage());
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }

    private class DonateButtonEditor extends DefaultCellEditor {
        private final JButton button = new JButton();
        private final int bloodBankId;

        public DonateButtonEditor(JCheckBox checkBox, int bloodBankId) {
            super(checkBox);
            this.bloodBankId = bloodBankId;
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String bloodGroup = (String) table.getValueAt(row, 1);
                    int donorId = donorIds.get(row);

                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
                        conn.setAutoCommit(false);
                        try {
                            // 1. Update stock
                            String stockQuery = "UPDATE bloodstock SET quantity = quantity + 1 WHERE bloodtype = ? AND BB_id = ?";
                            PreparedStatement ps1 = conn.prepareStatement(stockQuery);
                            ps1.setString(1, bloodGroup);
                            ps1.setInt(2, bloodBankId);
                            ps1.executeUpdate();

                            // 2. Insert into blooddonation
                            String donateQuery = "INSERT INTO blooddonation (quantity, donation_date, bloodtype, D_id, BB_id) VALUES (?, NOW(), ?, ?, ?)";
                            PreparedStatement ps2 = conn.prepareStatement(donateQuery);
                            ps2.setInt(1, 1);
                            ps2.setString(2, bloodGroup);
                            ps2.setInt(3, donorId);
                            ps2.setInt(4, bloodBankId);
                            ps2.executeUpdate();

                            conn.commit();
                            table.setValueAt("Donated", row, 2);
                            table.repaint();
                            JOptionPane.showMessageDialog(button, "Donation recorded and stock updated.");
                        } catch (Exception ex) {
                            conn.rollback();
                            throw ex;
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(button, "Error donating: " + ex.getMessage());
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value != null ? value.toString() : "Donate");
            return button;
        }

        public Object getCellEditorValue() {
            return "Donated";
        }
    }

    private class UploadButtonEditor extends DefaultCellEditor {
        private final JButton button = new JButton();

        public UploadButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0 && row < donorIds.size()) {
                    int donorId = donorIds.get(row);
                    JFileChooser chooser = new JFileChooser();
                    if (chooser.showOpenDialog(button) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        if (file.length() > 64 * 1024 * 1024) {
                            JOptionPane.showMessageDialog(button, "File exceeds 64MB size limit.");
                            return;
                        }
                        try (FileInputStream fis = new FileInputStream(file);
                            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
                            String sql = "INSERT INTO donorreports (D_id, report_name, report_data) VALUES (?, ?, ?)";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setInt(1, donorId);
                            ps.setString(2, file.getName());
                            ps.setBinaryStream(3, fis, (int) file.length());
                            ps.executeUpdate();
                            table.setValueAt("Uploaded", row, 3);
                            table.repaint();
                            JOptionPane.showMessageDialog(button, "Report uploaded.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(button, "Upload failed: " + ex.getMessage());
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value != null ? value.toString() : "Upload Image");
            return button;
        }

        public Object getCellEditorValue() {
            return "Uploaded";
        }
    }

    private class ViewButtonEditor extends DefaultCellEditor {
        private final JButton button = new JButton();

        public ViewButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row >= 0 && row < donorIds.size()) {
                    int donorId = donorIds.get(row);
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
                        String sql = "SELECT report_name, report_data FROM donorreports WHERE D_id = ? ORDER BY uploaded_at DESC LIMIT 1";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setInt(1, donorId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            String name = rs.getString("report_name");
                            byte[] data = rs.getBytes("report_data");
                            File file = new File("Donor_" + donorId + "_" + name);
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                fos.write(data);
                                Desktop.getDesktop().open(file);
                                table.setValueAt("Viewed", row, 4);
                                table.repaint();
                            }
                        } else {
                            JOptionPane.showMessageDialog(button, "No reports found.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(button, "Error displaying image: " + ex.getMessage());
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value != null ? value.toString() : "View Image");
            return button;
        }

        public Object getCellEditorValue() {
            return "Viewed";
        }
    }
}


private void showMyReportImage(int donorId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            String query = "SELECT report_data FROM donorreports WHERE D_id = ? ORDER BY uploaded_at DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, donorId);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                byte[] imgBytes = rs.getBytes("report_data");
                if (imgBytes != null) {
                    ImageIcon icon = new ImageIcon(imgBytes);
                    Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(image);
                    JLabel imgLabel = new JLabel(scaledIcon);
                    JOptionPane.showMessageDialog(null, imgLabel, "Your Report Image", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No image found.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No report uploaded yet.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading your report: " + ex.getMessage());
        }
    }
    

    
    private JPanel createBloodBankProfilePanel(int BBlogin_id) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Labels for Blood Bank profile data
        JLabel nameLabel = new JLabel("Blood Bank Name:");
        JLabel addressLabel = new JLabel("Location:");
        JLabel contactLabel = new JLabel("Contact Number:");
        JLabel emailLabel = new JLabel("Email:");

        // Labels to display fetched data
        JLabel nameValue = new JLabel();
        JLabel addressValue = new JLabel();
        JLabel contactValue = new JLabel();
        JLabel emailValue = new JLabel();

        // Add labels and values to the form
        formPanel.add(nameLabel);
        formPanel.add(nameValue);
        formPanel.add(addressLabel);
        formPanel.add(addressValue);
        formPanel.add(contactLabel);
        formPanel.add(contactValue);
        formPanel.add(emailLabel);
        formPanel.add(emailValue);

        // Fetch blood bank data from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            System.out.println("Connected to database.");
            System.out.println("Login ID: " + loginId);

            String query = "SELECT * FROM bloodbank WHERE bblogin_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loginId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Display data in the labels
                nameValue.setText(rs.getString("name"));
                addressValue.setText(rs.getString("location"));
                contactValue.setText(rs.getString("contact_no"));
                emailValue.setText(rs.getString("email"));
            } else {
                JOptionPane.showMessageDialog(panel, "No profile data found for this blood bank.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error fetching blood bank profile data.");
        }

        // Header
        panel.add(new JLabel("Blood Bank Profile", JLabel.CENTER), BorderLayout.NORTH);

        // Form Panel containing profile information
        panel.add(formPanel, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard"));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createBloodBankAppointmentListPanel(int bloodBankId) { 

        JPanel panel = new JPanel(new BorderLayout()); 

 

        JLabel titleLabel = new JLabel("Donor Appointments", JLabel.CENTER); 

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); 

        panel.add(titleLabel, BorderLayout.NORTH); 

 

        String[] columnNames = { "Appointment ID", "Donor Name", "Blood Type", "Phone", "Email", "Appointment Date" }; 

        DefaultTableModel model = new DefaultTableModel(columnNames, 0); 

        JTable appointmentTable = new JTable(model); 

        JScrollPane scrollPane = new JScrollPane(appointmentTable); 

        panel.add(scrollPane, BorderLayout.CENTER); 

 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            String query = "SELECT a.Appointment_id, a.date, d.first_name, d.last_name, d.bloodtype, d.ph_no, d.email " + 

                           "FROM appointment a " + 

                           "JOIN donor d ON a.D_id = d.D_id " + 

                           "WHERE a.BB_id = ? " + 

                           "ORDER BY a.date DESC"; 

            PreparedStatement stmt = conn.prepareStatement(query); 

            stmt.setInt(1, bloodBankId); 

            ResultSet rs = stmt.executeQuery(); 

 

            boolean hasAppointments = false; 

 

            while (rs.next()) { 

                int apptId = rs.getInt("Appointment_id"); 

                String fullName = rs.getString("first_name") + " " + rs.getString("last_name"); 

                String bloodType = rs.getString("bloodtype"); 

                String phone = rs.getString("ph_no"); 

                String email = rs.getString("email"); 

                Timestamp apptDate = rs.getTimestamp("date"); 

 

                model.addRow(new Object[] { apptId, fullName, bloodType, phone, email, apptDate }); 

                hasAppointments = true; 

            } 

 

            if (!hasAppointments) { 

                JOptionPane.showMessageDialog(panel, "No appointments found for this Blood Bank."); 

            } 

 

        } catch (SQLException e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(panel, "Error retrieving appointments: " + e.getMessage()); 

        } 

 

        JPanel bottomPanel = new JPanel(); 

        JButton backButton = new JButton("Back to Dashboard"); 

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard")); 

        bottomPanel.add(backButton); 

        panel.add(bottomPanel, BorderLayout.SOUTH); 

 

        return panel; 

    } 

    

    private JPanel createBloodStockPanel(int bloodBankId) { 

        JPanel panel = new JPanel(new BorderLayout()); 

 

        JLabel titleLabel = new JLabel("Blood Stock", JLabel.CENTER); 

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); 

        panel.add(titleLabel, BorderLayout.NORTH); 

 

        String[] columnNames = { "Blood Type", "Quantity (units)" }; 

        DefaultTableModel model = new DefaultTableModel(columnNames, 0); 

        JTable stockTable = new JTable(model); 

        JScrollPane scrollPane = new JScrollPane(stockTable); 

        panel.add(scrollPane, BorderLayout.CENTER); 

 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            // Debugging: Show the BB_id being used 

            System.out.println("BB_id being used: " + bloodBankId); 

 

            // Query all data to test if any data is fetched 

            String query = "SELECT bloodtype,quantity FROM BloodStock WHERE BB_id = ?"; 

            PreparedStatement stmt = conn.prepareStatement(query); 

            stmt.setInt(1, bloodBankId); 

            ResultSet rs = stmt.executeQuery(); 

            boolean hasData = false; 

            System.out.println("ResultSet Columns: " + rs.getMetaData().getColumnCount()); 

            System.out.println("ResultSet Column Names: " + rs.getMetaData().getColumnName(1) + ", " + rs.getMetaData().getColumnName(2) ); 

 

            while (rs.next()) { 

                System.out.println("Fetching row..."); 

                String bloodType = rs.getString("bloodtype"); 

                int quantity = rs.getInt("quantity"); 

              //  Date expiryDate = rs.getDate("expiry_date"); 

                System.out.println("Row: " + bloodType + ", " + quantity ); 

                model.addRow(new Object[] { bloodType, quantity }); 

                hasData = true; 

            } 

 

            if (!hasData) { 

                JOptionPane.showMessageDialog(panel, "No blood stock found for this Blood Bank."); 

            } 

 

        } catch (SQLException e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(panel, "SQL Error: " + e.getMessage()); 

        } 

        JPanel bottomPanel = new JPanel(); 

        JButton backButton = new JButton("Back to Dashboard"); 

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard")); 

        bottomPanel.add(backButton); 

        panel.add(bottomPanel, BorderLayout.SOUTH); 

        return panel; 

    } 


private JPanel createBloodBankSendNotificationsPanel(int bloodBankId) { 
    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints(); 
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.gridx = 0; 
    gbc.gridy = 0; 
    JLabel titleLabel = new JLabel("Send Notification"); 
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
    panel.add(titleLabel, gbc); 
    // Create buttons for different notification options 
    gbc.gridy++; 
    JButton toIndividualDonorBtn = new JButton("To Individual Donor"); 
    JButton toAllDonorsBtn = new JButton("To All Donors"); 
    JButton toIndividualHospitalBtn = new JButton("To Individual Hospital"); 
    JButton toAllHospitalsBtn = new JButton("To All Hospitals"); 
    // Add action listeners 
    toIndividualDonorBtn.addActionListener(e -> { 
    cardLayout.show(mainPanel, "SendNotificationToDonor"); 
    }); 
    toAllDonorsBtn.addActionListener(e -> { 
    cardLayout.show(mainPanel, "SendNotificationToAllDonors"); 
    }); 
    toIndividualHospitalBtn.addActionListener(e -> { 
    cardLayout.show(mainPanel, "SendNotificationToHospital"); 
    }); 
    toAllHospitalsBtn.addActionListener(e -> { 
    cardLayout.show(mainPanel, "SendNotificationToAllHospitals"); 
    }); 
    // Add buttons to panel 
    gbc.gridy++; 
    gbc.gridx = 0; 
    panel.add(toIndividualDonorBtn, gbc); 
    gbc.gridx = 1; 
    panel.add(toAllDonorsBtn, gbc); 
    gbc.gridy++; 
    gbc.gridx = 0; 
    panel.add(toIndividualHospitalBtn, gbc); 
    gbc.gridx = 1; 
    panel.add(toAllHospitalsBtn, gbc); 
    // Add Back button 
    gbc.gridy++; 
    gbc.gridx = 0; 
    gbc.gridwidth = 2; 
    JButton backButton = new JButton("Back"); 
    backButton.addActionListener(e -> { 
    cardLayout.show(mainPanel, "BloodBankDashboard"); 
    }); 
    panel.add(backButton, gbc); 
    return panel; 
    } 
    private JPanel createSendNotificationToDonorPanel(int bloodBankId) { 
    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints(); 
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.gridx = 0; gbc.gridy = 0; 
    panel.add(new JLabel("Select Donor:"), gbc); 
    gbc.gridx = 1; 
    JComboBox<String> donorComboBox = new JComboBox<>(); 
    Vector<Integer> donorIds = new Vector<>(); 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String sql = "SELECT D_id, first_name, last_name FROM donor"; 
    PreparedStatement stmt = conn.prepareStatement(sql); 
    ResultSet rs = stmt.executeQuery(); 
    while (rs.next()) { 
    int donorId = rs.getInt("D_id"); 
    String name = rs.getString("first_name") + " " + rs.getString("last_name"); 
    donorComboBox.addItem(name); 
    donorIds.add(donorId); 
    } 
    } catch (Exception e) { 
    e.printStackTrace(); 
    } 
    panel.add(donorComboBox, gbc); 
    gbc.gridx = 0; gbc.gridy++; 
    panel.add(new JLabel("Message:"), gbc); 
    gbc.gridx = 1; 
    JTextArea messageArea = new JTextArea(5, 20); 
    panel.add(new JScrollPane(messageArea), gbc); 
    gbc.gridy++; 
    JButton sendBtn = new JButton("Send Notification"); 
    JLabel statusLabel = new JLabel(""); 
    sendBtn.addActionListener(e -> { 
    int selectedDonorId = donorIds.get(donorComboBox.getSelectedIndex()); 
    String message = messageArea.getText().trim(); 
    if (!message.isEmpty()) { 
    sendNotificationToDonor(selectedDonorId, message); 
    statusLabel.setText("Notification sent!"); 
    } else { 
    statusLabel.setText("Message cannot be empty."); 
    } 
    }); 
    panel.add(sendBtn, gbc); 
    gbc.gridy++; 
    panel.add(statusLabel, gbc); 
    gbc.gridy++; 
    JButton backBtn = new JButton("Back"); 
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankSendNotifications")); 
    panel.add(backBtn, gbc); 
    return panel; 
    } 
   
    private void sendNotificationToDonor(String message, int donorId, int bloodbankId) { 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String sql = "INSERT INTO notification (message, D_id, BB_id, status) VALUES (?, ?, ?, ?)"; 
    PreparedStatement stmt = conn.prepareStatement(sql); 
    stmt.setString(1, message); 
    stmt.setInt(2, donorId); 
    stmt.setInt(3, bloodbankId); 
    stmt.setString(4, "Sent"); 
    stmt.executeUpdate(); 
    JOptionPane.showMessageDialog(null, "Notification sent to Donor!"); 
    } catch (Exception e) { 
    e.printStackTrace(); 
    JOptionPane.showMessageDialog(null, "Error sending notification."); 
    } 
    } 
    private JPanel createSendNotificationToHospitalPanel(int bloodBankId) { 
    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints(); 
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.gridx = 0; gbc.gridy = 0; 
    panel.add(new JLabel("Select Hospital:"), gbc); 
    gbc.gridx = 1; 
    JComboBox<String> hospitalComboBox = new JComboBox<>(); 
    Vector<Integer> hospitalIds = new Vector<>(); 
    // Load hospitals from DB 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String query = "SELECT H_id, name FROM hospitalregister"; 
    PreparedStatement stmt = conn.prepareStatement(query); 
    ResultSet rs = stmt.executeQuery(); 
    while (rs.next()) { 
    hospitalIds.add(rs.getInt("H_id")); 
    hospitalComboBox.addItem(rs.getString("name")); 
    } 
    } catch (Exception e) { 
    e.printStackTrace(); 
    } 
    panel.add(hospitalComboBox, gbc); 
    // Message field 
    gbc.gridx = 0; gbc.gridy++; 
    panel.add(new JLabel("Message:"), gbc); 
    gbc.gridx = 1; 
    JTextArea messageArea = new JTextArea(5, 20); 
    panel.add(new JScrollPane(messageArea), gbc); 
    // Send button 
    gbc.gridy++; 
    JButton sendBtn = new JButton("Send Notification"); 
    JLabel statusLabel = new JLabel(); 
    panel.add(sendBtn, gbc); 
    // Status label 
    gbc.gridy++; 
    panel.add(statusLabel, gbc); 
    // Back button 
    gbc.gridy++; 
    JButton backBtn = new JButton("Back"); 
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankSendNotifications")); 
    panel.add(backBtn, gbc); 
    // Send button action 
    sendBtn.addActionListener(e -> { 
    int selectedHospitalId = hospitalIds.get(hospitalComboBox.getSelectedIndex()); 
    String message = messageArea.getText().trim(); 
    if (!message.isEmpty()) { 
    sendNotificationToHospital(selectedHospitalId, bloodBankId, message); 
    statusLabel.setText("Notification sent!"); 
    } else { 
    statusLabel.setText("Message cannot be empty."); 
    } 
    }); 
    return panel; 
    } 
    public void sendNotificationToHospital(int hospitalId, int bloodBankId, String message) { 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String query = "INSERT INTO HospitalNotification (H_id, BB_id, message, status) VALUES (?, ?, ?, 'unread')"; 
    PreparedStatement stmt = conn.prepareStatement(query); 
    stmt.setInt(1, hospitalId); 
    stmt.setInt(2, bloodBankId); 
    stmt.setString(3, message); 
    stmt.executeUpdate(); 
    System.out.println("Notification sent to hospital."); 
    } catch (Exception e) { 
    e.printStackTrace(); 
    } 
    } 
    public void sendNotificationToDonor(int donorId, String message) { 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String query = "INSERT INTO Notification (D_id, message, status) VALUES (?, ?, 'unread')"; 
    PreparedStatement stmt = conn.prepareStatement(query); 
    stmt.setInt(1, donorId); 
    stmt.setString(2, message); 
    int rows = stmt.executeUpdate(); 
    if (rows > 0) { 
    System.out.println("Notification sent successfully."); 
    } 
    } catch (Exception e) { 
    e.printStackTrace(); 
    } 
    } 
    private void sendNotificationToAllDonors(String message) { 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String query = "INSERT INTO Notification (D_id, message, status) " + 
    "SELECT D_id, ?, 'unread' FROM donor"; 
    PreparedStatement stmt = conn.prepareStatement(query); 
    stmt.setString(1, message); 
    int rows = stmt.executeUpdate(); 
    if (rows > 0) { 
    JOptionPane.showMessageDialog(null, "Notification sent to all donors successfully!"); 
    } 
    } catch (Exception e) { 
    e.printStackTrace(); 
    JOptionPane.showMessageDialog(null, "Error sending notifications to all donors."); 
    } 
    } 
    private void sendNotificationToAllHospitals(String message) { 
    try (Connection conn = 
    DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", 
    "WJ28@krhps")) { 
    String query = "INSERT INTO HospitalNotification (H_id, message, status) " + 
    "SELECT H_id, ?, 'unread' FROM hospitalregister"; 
    PreparedStatement stmt = conn.prepareStatement(query); 
    stmt.setString(1, message); 
    int rows = stmt.executeUpdate(); 
    if (rows > 0) { 
    JOptionPane.showMessageDialog(null, "Notification sent to all hospitals successfully!"); 
    } 
    } catch (Exception e) { 
    e.printStackTrace(); 
    JOptionPane.showMessageDialog(null, "Error sending notifications to all hospitals."); 
    } 
    } 
    private JPanel createSendNotificationToAllDonorsPanel(int bloodBankId)  
    { 
    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints(); 
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.gridx = 0; 
    gbc.gridy = 0; 
    // Title 
    JLabel titleLabel = new JLabel("Send Notification to All Donors"); 
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
    panel.add(titleLabel, gbc); 
    // Message field 
    gbc.gridy++; 
    panel.add(new JLabel("Message:"), gbc); 
    gbc.gridx = 1; 
    JTextArea messageArea = new JTextArea(5, 20); 
    panel.add(new JScrollPane(messageArea), gbc); 
    // Send button 
    gbc.gridy++; 
    gbc.gridx = 0; 
    gbc.gridwidth = 2; 
    JButton sendBtn = new JButton("Send to All Donors"); 
    JLabel statusLabel = new JLabel(""); 
    panel.add(sendBtn, gbc); 
    // Status label 
    gbc.gridy++; 
    panel.add(statusLabel, gbc); 
    // Back button 
    gbc.gridy++; 
    JButton backBtn = new JButton("Back"); 
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankSendNotifications")); 
    panel.add(backBtn, gbc); 
    // Send button action 
    sendBtn.addActionListener(e -> { 
    String message = messageArea.getText().trim(); 
    if (!message.isEmpty()) { 
    sendNotificationToAllDonors(message); 
    statusLabel.setText("Notification sent to all donors!"); 
    } else { 
    statusLabel.setText("Message cannot be empty."); 
    } 
    }); 
    return panel; 
    } 
    private JPanel createSendNotificationToAllHospitalsPanel(int bloodBankId) { 
    JPanel panel = new JPanel(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints(); 
    gbc.insets = new Insets(10, 10, 10, 10); 
    gbc.gridx = 0; 
    gbc.gridy = 0; 
    // Title 
    JLabel titleLabel = new JLabel("Send Notification to All Hospitals"); 
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
    panel.add(titleLabel, gbc); 
    // Message field 
    gbc.gridy++; 
    panel.add(new JLabel("Message:"), gbc); 
    gbc.gridx = 1; 
    JTextArea messageArea = new JTextArea(5, 20); 
    panel.add(new JScrollPane(messageArea), gbc); 
    // Send button 
    gbc.gridy++; 
    gbc.gridx = 0; 
    gbc.gridwidth = 2; 
    JButton sendBtn = new JButton("Send to All Hospitals"); 
    JLabel statusLabel = new JLabel(""); 
    panel.add(sendBtn, gbc); 
    // Status label 
    gbc.gridy++; 
    panel.add(statusLabel, gbc); 
    // Back button 
    gbc.gridy++; 
    JButton backBtn = new JButton("Back"); 
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankSendNotifications")); 
    panel.add(backBtn, gbc); 
    // Send button action 
    sendBtn.addActionListener(e -> { 
    String message = messageArea.getText().trim(); 
    if (!message.isEmpty()) { 
    sendNotificationToAllHospitals(message); 
    statusLabel.setText("Notification sent to all hospitals!"); 
    } else { 
    statusLabel.setText("Message cannot be empty."); 
    } 
    }); 
    return panel; 
    } 
private JPanel createBloodBankViewNotificationsPanel(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Notifications from Hospitals", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
    
        String[] columnNames = {"Message", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) {
            
            
            PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE BloodBankNotification SET status = 'read' WHERE BB_id = ? AND status = 'unread'");
            updateStmt.setInt(1, bloodBankId);
            updateStmt.executeUpdate();
    
           
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT message, status FROM BloodBankNotification WHERE BB_id = ?");
            stmt.setInt(1, bloodBankId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String message = rs.getString("message");
                String status = rs.getString("status");  // Now it will show 'read'
                model.addRow(new Object[]{message, status});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard"));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    
        return panel;
    }
    
    private JTextField addField(JPanel panel, String label) { 

        panel.add(new JLabel(label)); 

        JTextField field = new JTextField(); 

        panel.add(field); 

        return field; 

    } 

    // Handle Login 

    private void handleLogin() { 

        String username = loginUsername.getText(); 

        String password = String.valueOf(loginPassword.getPassword()); 

 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            String sql = "SELECT * FROM login WHERE username=? AND password=?"; 

            PreparedStatement stmt = conn.prepareStatement(sql); 

            stmt.setString(1, username); 

            stmt.setString(2, password); 

            ResultSet rs = stmt.executeQuery(); 

 

            if (rs.next()) { 

                String role = rs.getString("role"); 

                int loginId = rs.getInt("id"); 

                this.loginId = loginId; 

 

                if (role.equalsIgnoreCase("donor")) { 

                    int donorId = -1; 

                    String donorQuery = "SELECT D_id, first_name, last_name FROM donor WHERE login_id = ?"; 

                    PreparedStatement donorStmt = conn.prepareStatement(donorQuery); 

                    donorStmt.setInt(1, loginId); 

                    ResultSet donorRs = donorStmt.executeQuery(); 

                    if (donorRs.next()) { 

                        donorId = donorRs.getInt("D_id"); 

                        String firstName = donorRs.getString("first_name"); 

                        String lastName = donorRs.getString("last_name"); 

                        welcomeLabel.setText("WELCOME, " + firstName + " " + lastName); 

                    } 

 

                    mainPanel.add(createDonorProfilePanel(loginId), "DonorProfile"); 

                    mainPanel.add(createAppointmentBookingPanel(donorId), "BookAppointment"); 

                    mainPanel.add(createDonationHistoryPanel(donorId), "DonationHistory"); 

                    mainPanel.add(createReceiveNotificationPanel(donorId), "Notifications"); 

                    mainPanel.add(createAppointmentHistoryPanel(donorId), "AppointmentHistory"); 

                    mainPanel.add(createViewReportsPanel(donorId), "ViewReport"); 

 
                    mainPanel.add(createDonorDashboard(), "DonorDashboard");  // 🔥 Add this line

cardLayout.show(mainPanel, "DonorDashboard");             


                } else if (role.equalsIgnoreCase("hospital")) { 

                    int hospitalId = -1; 

                    String hospitalQuery = "SELECT H_id, name FROM hospitalregister WHERE Hlogin_id = ?"; 

                    PreparedStatement hospitalStmt = conn.prepareStatement(hospitalQuery); 

                    hospitalStmt.setInt(1, loginId); 

                    ResultSet hospitalRs = hospitalStmt.executeQuery(); 

                    if (hospitalRs.next()) { 

                        hospitalId = hospitalRs.getInt("H_id"); 

                        welcomeLabel.setText("WELCOME, " + hospitalRs.getString("name")); 

                    } 

 

                    mainPanel.add(createHospitalProfilePanel(hospitalId), "HospitalProfile"); 

                    mainPanel.add(createBloodRequestPanel(hospitalId), "BloodRequest"); 
                    mainPanel.add(createHospitalRequestHistoryPanel(hospitalId),"viewbloodRequestHistory");

                    mainPanel.add(createHospitalNotificationPanel(hospitalId), "Notifications"); 


                    mainPanel.add(createSendNotificationToDonorPanel(hospitalId), "HospitalSendNotificationToDonor"); 

                    mainPanel.add(createHospitalDashboardPanel(hospitalId), "HospitalDashboard"); 

 

                    cardLayout.show(mainPanel, "HospitalDashboard"); 

 

                } else if (role.equalsIgnoreCase("bloodbank")) {
                    int bloodbankId = -1;
                    String bbQuery = "SELECT BB_id, name FROM bloodbank WHERE BBlogin_id = ?";
                    PreparedStatement bbStmt = conn.prepareStatement(bbQuery);
                    bbStmt.setInt(1, loginId);
                    ResultSet bbRs = bbStmt.executeQuery();
                    if (bbRs.next()) {
                        bloodbankId = bbRs.getInt("BB_id");
                        welcomeLabel.setText("WELCOME, " + bbRs.getString("name"));
                    }

                    mainPanel.add(createBloodBankProfilePanel(bloodbankId), "BloodBankProfile");
                    mainPanel.add(createBloodBankAppointmentListPanel(bloodbankId), "BloodBankAppointmentList");
                    mainPanel.add(createBloodStockPanel(bloodbankId), "BloodStock");
                    mainPanel.add(createSendNotificationToDonorPanel(bloodbankId), "SendNotificationToDonor");
                    mainPanel.add(createSendNotificationToHospitalPanel(bloodbankId), "SendNotificationToHospital");
                    mainPanel.add(createBloodBankSendNotificationsPanel(bloodbankId), "BloodBankSendNotifications");
                    mainPanel.add(createBloodBankViewNotificationsPanel(bloodbankId), "BloodBankViewNotifications");
                    mainPanel.add(createBloodBankDashboard(bloodbankId), "BloodBankDashboard");
                  
                    mainPanel.add(createSendNotificationToAllDonorsPanel(bloodbankId), "SendNotificationToAllDonors");
                    mainPanel.add(createSendNotificationToAllHospitalsPanel(bloodbankId), "SendNotificationToAllHospitals");

                    cardLayout.show(mainPanel, "BloodBankDashboard");
                }

 

            } else { 

                JOptionPane.showMessageDialog(this, "Invalid username or password"); 

            } 

        } catch (Exception e) { 

            e.printStackTrace(); 

            JOptionPane.showMessageDialog(this, "Connection Error: " + e.getMessage()); 

        } 

    } 

 

    // Handle Donor Registration 

     

    private void registerDonor() { 

        // Generate a simple username and default password 

        String username = fnameField.getText().toLowerCase().concat(lnameField.getText().toLowerCase().replace(" ", "_")); 

        String password = dobField.getText().replace("-", ""); 

        String phone = phoneField.getText().trim(); 

 

        // Validate phone number 

        if (!phone.matches("\\d{10}")) { 

            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits."); 

            return; 

        }
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

            conn.setAutoCommit(false); 

            String loginSQL = "INSERT INTO login (username, password, role) VALUES (?, ?, 'donor')"; 

            PreparedStatement loginStmt = conn.prepareStatement(loginSQL, Statement.RETURN_GENERATED_KEYS); 

            loginStmt.setString(1, username); 

            loginStmt.setString(2, password); 

            loginStmt.executeUpdate(); 

            ResultSet rs = loginStmt.getGeneratedKeys(); 

            int loginId = -1; 

            if (rs.next()) { 

                loginId = rs.getInt(1); 

            } 

            if (loginId == -1) throw new SQLException("Failed to retrieve login ID."); 


            String donorSQL = "INSERT INTO donor (login_id, first_name, last_name, address, dob, donated_date, bloodtype, ph_no, email, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 

            PreparedStatement donorStmt = conn.prepareStatement(donorSQL); 

            donorStmt.setInt(1, loginId); 

            donorStmt.setString(2, fnameField.getText()); 

            donorStmt.setString(3, lnameField.getText()); 

            donorStmt.setString(4, addressField.getText()); 

            donorStmt.setDate(5, Date.valueOf(dobField.getText())); 

            String donatedDateText = donatedDateField.getText().trim(); 

            if (!donatedDateText.isEmpty()) { 

                donorStmt.setDate(6, Date.valueOf(donatedDateText)); 

            } else { 

                donorStmt.setNull(6, Types.DATE); 

            } 

            donorStmt.setString(7, bloodTypeBox.getSelectedItem().toString()); 

            donorStmt.setString(8, phoneField.getText()); 

            donorStmt.setString(9, emailField.getText()); 

            donorStmt.setInt(10, Integer.parseInt(ageField.getText())); 

            donorStmt.executeUpdate(); 

 

            conn.commit(); 

            JOptionPane.showMessageDialog(this, "Registration successful!\n ' Your Username:' " + username + "\n'Your Password:' " + password+ "\n Please enter the above credentials to login next time "); 

            cardLayout.show(mainPanel, "Login"); 

 

        } catch (Exception ex) { 

            ex.printStackTrace(); 

            JOptionPane.showMessageDialog(this, "Registration Error: " + ex.getMessage()); 

        } 

    } 

     

    private void hospitalregister() { 

    	 

    		String username = HospitalNameField.getText().toLowerCase().replaceAll("\\s+", ""); 

    		String password = "hospital123";  

    		 

    		try(Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "WJ28@krhps")) { 

                conn.setAutoCommit(false); 

    		 

    		String loginSQL = "INSERT INTO login(username,password,role) values (?,?,'hospital')"; 

    		PreparedStatement loginstmt = conn.prepareStatement(loginSQL, Statement.RETURN_GENERATED_KEYS); 

    		loginstmt.setString(1, username); 

    		loginstmt.setString(2, password); 

    		loginstmt.executeUpdate(); 

    		 

    		ResultSet rs = loginstmt.getGeneratedKeys(); 

    		int loginId=-1; 

    		if(rs.next()) { 

    			loginId = rs.getInt(1); 

    		} 

    		if(loginId==-1)throw new SQLException("Failed to retrieve login ID."); 

    		 

    		String hospitalSQL = "INSERT INTO hospital (Hlogin_id, name, location, email, contact_no) VALUES (?, ?, ?, ?, ?)"; 

            PreparedStatement hospStmt = conn.prepareStatement(hospitalSQL); 

            hospStmt.setInt(1, loginId); 

            hospStmt.setString(2, HospitalNameField.getText()); 

            hospStmt.setString(3, HospitalLocationField.getText()); 

            hospStmt.setString(4, HospitalEmailField.getText()); 

            hospStmt.setString(5, HospitalContactnoField.getText()); 

            hospStmt.executeUpdate(); 

             

            conn.commit(); 

            JOptionPane.showMessageDialog(this, "Hospital Registered!\nUsername: " + username + "\nPassword: hospital123"); 

            cardLayout.show(mainPanel, "Login"); 

             

    		} catch (Exception ex) { 

                ex.printStackTrace(); 

                JOptionPane.showMessageDialog(this, "Hospital Registration Error: " + ex.getMessage()); 

            } 

        } 

    	 

    public static void main(String[] args) { 

        new BloodDonationApp(); 

    } 

} 


 


