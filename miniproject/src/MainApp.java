

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class MainApp extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    JLabel welcomeLabel;
    // Login components
    JTextField loginUsername;
    JPasswordField loginPassword;

    // Donor registration components
    JTextField fnameField, lnameField, addressField, dobField, donatedDateField, phoneField, emailField, ageField,HospitalNameField,HospitalLocationField,HospitalEmailField,HospitalContactnoField;
    JComboBox<String> bloodTypeBox;
	JTextField BloodBankNameField,BloodBankLocationField,BloodBankEmailField,BloodBankContactnoField;
	private int loginId;
	private int loggedInUserId;
	private JComboBox<String> hospitalNameBox;

	 

    public MainApp() {
        setTitle("Blood Donation Application");
        setSize(450, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add login and registration panels
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createDonorRegistrationPanel(), "Register as DONOR");
        mainPanel.add(createDonorDashboard(), "DonorDashboard");
        
        
        mainPanel.add(createHospitalRegistrationPanel(),"Register as HOSPITAL");
        
        
        mainPanel.add(createBloodBankRegistrationPanel(),"Register as BLOOD BANK");
        mainPanel.add(createBloodBankDashboard(1), "BloodBankDashboard");        
        
        
        

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
        setVisible(true);
    }

    // Login Panel
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

    // Donor Registration Panel
    private JPanel createDonorRegistrationPanel() {
        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));

        fnameField = addField(panel, "First Name:");
        lnameField = addField(panel, "Last Name:");
        addressField = addField(panel, "Address:");
        dobField = addField(panel, "DOB (YYYY-MM-DD):");
        dobField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    java.time.LocalDate dob = java.time.LocalDate.parse(dobField.getText());
                    java.time.LocalDate now = java.time.LocalDate.now();
                    int age = java.time.Period.between(dob, now).getYears();
                    ageField.setText(String.valueOf(age));
                } catch (Exception ex) {
                    ageField.setText("");
                    // Optionally show error
                    JOptionPane.showMessageDialog(null, "Invalid date format. Use YYYY-MM-DD");
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

        registerBtn.addActionListener(e -> registerDonor());
        backToLogin.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        panel.add(registerBtn);
        panel.add(backToLogin);

        return panel;
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
        JButton logoutBtn = new JButton("Logout");
        
        profileBtn.addActionListener(e -> cardLayout.show(mainPanel, "DonorProfile"));
        bookApptBtn.addActionListener(e -> cardLayout.show(mainPanel, "BookAppointment"));
        apptHistoryBtn.addActionListener(e -> cardLayout.show(mainPanel, "AppointmentHistory"));
        donationHistoryBtn.addActionListener(e -> cardLayout.show(mainPanel, "DonationHistory"));
        notificationBtn.addActionListener(e -> cardLayout.show(mainPanel, "Notifications"));
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
        
        buttonPanel.add(profileBtn);
        buttonPanel.add(bookApptBtn);
        buttonPanel.add(apptHistoryBtn);
        buttonPanel.add(donationHistoryBtn);
        buttonPanel.add(notificationBtn);
        buttonPanel.add(logoutBtn);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Add more donor-specific functionality here (view donations, request history, etc.)
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

        // Add labels and values to the form
        formPanel.add(nameLabel); formPanel.add(nameValue);
        formPanel.add(addressLabel); formPanel.add(addressValue);
        formPanel.add(dobLabel); formPanel.add(dobValue);
        formPanel.add(donatedDateLabel); formPanel.add(donatedDateValue);
        formPanel.add(bloodTypeLabel); formPanel.add(bloodTypeValue);
        formPanel.add(phoneLabel); formPanel.add(phoneValue);
        formPanel.add(emailLabel); formPanel.add(emailValue);
        formPanel.add(ageLabel); formPanel.add(ageValue);

        // Fetch donor data from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
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
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
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
        gbc.gridx = 1;
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
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            String query = "SELECT donation_date, bloodtype, BloodDonation_id FROM BloodDonation WHERE D_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, donorId);  // Use the donorId passed as argument

            ResultSet rs = stmt.executeQuery();
            
            // Check if there are any results and display them
            while (rs.next()) {
                String donationDate = rs.getString("donation_date");
                String bloodType = rs.getString("bloodtype");
                String donationId = rs.getString("BloodDonation_id");

                // Create a label for each donation and add it to the panel
                String donationInfo = "Donation ID: " + donationId + " | Date: " + donationDate + " | Blood Type: " + bloodType;
                JLabel donationLabel = new JLabel(donationInfo);
                historyPanel.add(donationLabel);
            }
            
            if (historyPanel.getComponentCount() == 0) {
                // Show a message if no donations found
                historyPanel.add(new JLabel("No donation history found."));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error fetching donation history: " + e.getMessage());
        }

        // Add history panel to the main panel
        panel.add(new JScrollPane(historyPanel), BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DonorDashboard"));
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

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
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

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
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
    
    
    
    private JPanel createReceiveNotificationPanelForBloodBank(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // DefaultListModel for storing notifications
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> notificationList = new JList<>(model);
        
        // Panel setup
        panel.add(new JLabel("Blood Bank Notifications", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(notificationList), BorderLayout.CENTER);

        // Back Button to navigate back to the dashboard
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard"));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Fetching notifications from the database for the specified Blood Bank
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            String query = "SELECT message, status, created_at FROM Notification WHERE BB_id = ? ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bloodBankId);
            ResultSet rs = stmt.executeQuery();

            // Adding notifications to the list model
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

    


    private JPanel createHospitalRegistrationPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
    
        String[] mysoreHospitals = {
            "JSS Hospital", "Apollo BGS", "K.R. Hospital", "Narayana Multispeciality", "Skanray Hospital","Manipal Hospital","Jayadev Hospital","Chandrakala Hospital"
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
        panel.add(new JLabel("Email (@gmail.com only):"));
        panel.add(HospitalEmailField);
        panel.add(new JLabel("Contact No (10 digits):"));
        panel.add(HospitalContactnoField);
        panel.add(new JLabel("Set Password:"));
        panel.add(HospitalPasswordField);
    
        JButton registerBtn = new JButton("Register Hospital");
        JButton backBtn = new JButton("Back to Login");
    
        registerBtn.addActionListener(e -> {
            String email = HospitalEmailField.getText();
            String contact = HospitalContactnoField.getText();
            String password = new String(HospitalPasswordField.getPassword());
    
            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Email must end with @gmail.com");
                return;
            }
    
            if (!contact.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Contact number must be exactly 10 digits");
                return;
            }
    
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty");
                return;
            }
    
            hospitalregister(password);
        });
    
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

    
        panel.add(registerBtn);
        panel.add(backBtn);
    
        return panel;
    }

    private void hospitalregister(String password) {
        String username = hospitalNameBox.getSelectedItem().toString().toLowerCase().replaceAll("\\s+", "");
        
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            conn.setAutoCommit(false);
    
            // Step 1: Insert into login table
            String loginSQL = "INSERT INTO login(username, password, role) VALUES (?, ?, 'hospital')";
            PreparedStatement loginstmt = conn.prepareStatement(loginSQL, Statement.RETURN_GENERATED_KEYS);
            loginstmt.setString(1, username);
            loginstmt.setString(2, password);
            loginstmt.executeUpdate();
    
            ResultSet rs = loginstmt.getGeneratedKeys();
            int loginId = -1;
            if (rs.next()) {
                loginId = rs.getInt(1);
            }
            if (loginId == -1) throw new SQLException("Failed to retrieve login ID.");
    
            // Step 2: Insert into hospital table
            String hospitalSQL = "INSERT INTO hospitalregister (Hlogin_id, name, location, email, contact_no) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement hospStmt = conn.prepareStatement(hospitalSQL);
            hospStmt.setInt(1, loginId);
            hospStmt.setString(2, hospitalNameBox.getSelectedItem().toString());
            hospStmt.setString(3, HospitalLocationField.getText());
            hospStmt.setString(4, HospitalEmailField.getText());
            hospStmt.setString(5, HospitalContactnoField.getText());
            hospStmt.executeUpdate();
    
            conn.commit();
            JOptionPane.showMessageDialog(this, "Hospital Registered!\nUsername: " + username + "\nPassword: " + password);
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
    
    
    private JPanel createHospitalDashboardPanel(int hospitalId) 
    {
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Hospital Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
    
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // Adjust layout as needed
    
        JButton profileBtn = new JButton("View Hospital Profile");
        JButton requestHistoryBtn = new JButton("View Blood Request History");
        JButton notificationBtn = new JButton("Notifications");
        JButton requestBloodBtn = new JButton("Request Blood");
        JButton logoutBtn = new JButton("Logout");
    
        //  Add action listeners (these will now just show existing panels)
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "WJ28@krhps")) {
            String sql = "SELECT name, location, email, contact_no FROM hospitalregister WHERE Hlogin_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, hospitalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                panel.add(new JLabel("Name:")); panel.add(new JLabel(rs.getString("name")));
                panel.add(new JLabel("Location:")); panel.add(new JLabel(rs.getString("location")));
                panel.add(new JLabel("Email:")); panel.add(new JLabel(rs.getString("email")));
                panel.add(new JLabel("Contact No:")); panel.add(new JLabel(rs.getString("contact_no")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalDashboard"));
        panel.add(backBtn);
        return panel;
    }
    
    private JPanel createBloodRequestPanel(int hospitalId) {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField patientNameField = new JTextField();
        JComboBox<String> bloodTypeBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JTextField requestDateField = new JTextField(LocalDate.now().toString());
        
        panel.add(new JLabel("Patient Name:")); panel.add(patientNameField);
        panel.add(new JLabel("Blood Type:")); panel.add(bloodTypeBox);
        panel.add(new JLabel("Request Date (YYYY-MM-DD):")); panel.add(requestDateField);
    
        JButton sendBtn = new JButton("Request Blood");
        JButton backBtn = new JButton("Back");
    
        sendBtn.addActionListener(e -> {
            String patientName = patientNameField.getText();
            String bloodType = (String) bloodTypeBox.getSelectedItem();
            String requestDate = requestDateField.getText();
    
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "WJ28@krhps")) {
                // Store in BloodRequest
                String insertReq = "INSERT INTO BloodRequest (H_id, patient_name, blood_type, request_date) VALUES (?, ?, ?, ?)";
                PreparedStatement reqStmt = conn.prepareStatement(insertReq);
                reqStmt.setInt(1, hospitalId);
                reqStmt.setString(2, patientName);
                reqStmt.setString(3, bloodType);
                reqStmt.setDate(4, Date.valueOf(requestDate));
                reqStmt.executeUpdate();
    
                // Notify all blood banks (or specific if needed)
                Statement getBB = conn.createStatement();
                ResultSet bbRs = getBB.executeQuery("SELECT BB_id FROM BloodBank");
                while (bbRs.next()) {
                    int bbId = bbRs.getInt("BB_id");
                    String message = "New Blood Request: " + bloodType + " for " + patientName + " on " + requestDate;
                    PreparedStatement notifyStmt = conn.prepareStatement("INSERT INTO BloodBankNotification (BB_id, message, status) VALUES (?, ?, 'unread')");
                    notifyStmt.setInt(1, bbId);
                    notifyStmt.setString(2, message);
                    notifyStmt.executeUpdate();
                }
    
                JOptionPane.showMessageDialog(panel, "Blood request sent successfully!");
    
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error sending blood request.");
            }
        });
    
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HospitalDashboard"));
        panel.add(sendBtn); panel.add(backBtn);
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
        model.addColumn("Status");
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "WJ28@krhps")) {
            String query = "SELECT * FROM hospitalrequest WHERE H_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, hospitalId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("request_id"),
                    rs.getString("patient_name"),
                    rs.getString("blood_type"),
                    rs.getDate("request_date"),
                    rs.getString("status")
                });
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "WJ28@krhps")) {
            String sql = "SELECT message, created_at FROM BloodBankNotification WHERE BB_id IN (SELECT BB_id FROM BloodBank) ORDER BY created_at DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String msg = "[" + rs.getTimestamp("created_at") + "] " + rs.getString("message");
                model.addElement(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return panel;
    }
    
    
    
    
    private JPanel createBloodBankRegistrationPanel() {
    	JPanel panel = new JPanel(new GridLayout(9,2,10,10));
    	
    	
    	BloodBankNameField = new JTextField();
    	BloodBankLocationField = new JTextField();
    	BloodBankEmailField = new JTextField();
    	BloodBankContactnoField = new JTextField();
    	
    	panel.add(new JLabel("Blood bank Name: "));
    	panel.add(BloodBankNameField);
    	panel.add(new JLabel("Blood bank Location: "));
    	panel.add(BloodBankLocationField);
    	panel.add(new JLabel("Blood bank Email:"));
    	panel.add(BloodBankEmailField);
    	panel.add(new JLabel("Blood bank Contact no: "));
    	panel.add(BloodBankContactnoField);
    	
    	JButton registerBtn = new JButton("Register Blood bank");
    	JButton backbtn = new JButton("Back to login");
    	registerBtn.addActionListener(e -> BloodBankRegister());
        backbtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

    	
    	panel.add(registerBtn);
    	panel.add(backbtn);
    	
    	return panel;
    }
    
    
    private JPanel createBloodBankDashboard(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome Blood Bank", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton profileBtn = new JButton("View Profile");
        JButton appointmentsBtn = new JButton("Appointments");
        JButton bloodStockBtn = new JButton("Blood Stock");
        JButton transactionsBtn = new JButton("Requested Blood - Transactions");
        JButton logoutBtn = new JButton("Logout");

        profileBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankProfile"));
        appointmentsBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankAppointmentList"));
        bloodStockBtn.addActionListener(e -> cardLayout.show(mainPanel, "BloodStock"));
        transactionsBtn.addActionListener(e -> cardLayout.show(mainPanel, "RequestedBlood"));
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        buttonPanel.add(profileBtn);
        buttonPanel.add(appointmentsBtn);
        buttonPanel.add(bloodStockBtn);
        buttonPanel.add(transactionsBtn);
        buttonPanel.add(logoutBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createBloodBankProfilePanel(int loginId) {
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
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
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Book Appointment for Blood Bank");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        // Fetch donor IDs and Names from the database (for appointment creation)
        Vector<String> donors = new Vector<>();
        Vector<Integer> donorIds = new Vector<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            String query = "SELECT D_id, first_name, last_name FROM donor";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                donorIds.add(rs.getInt("D_id"));
                donors.add(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Donor dropdown
        gbc.gridy++;
        panel.add(new JLabel("Select Donor:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> donorDropdown = new JComboBox<>(donors);
        panel.add(donorDropdown, gbc);

        // Date & Time input
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Date & Time (YYYY-MM-DD HH:MM:SS):"), gbc);
        gbc.gridx = 1;
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
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
                    int selectedDonorId = donorIds.get(donorDropdown.getSelectedIndex());
                    String dateTimeStr = datetimeField.getText().trim();

                    if (dateTimeStr.isEmpty()) {
                        statusLabel.setText("Please enter a valid date and time.");
                        return;
                    }

                    LocalDate appointmentDate = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();

                    // Prevent booking in the past
                    if (appointmentDate.isBefore(LocalDate.now())) {
                        statusLabel.setText("Appointment date cannot be in the past.");
                        return;
                    }

                    LocalDate lastDonationDate = null;

                    // Step 1: Try to get the latest appointment date for this donor
                    String latestApptQuery = "SELECT MAX(date) AS latest_date FROM Appointment WHERE D_id = ?";
                    PreparedStatement apptStmt = conn.prepareStatement(latestApptQuery);
                    apptStmt.setInt(1, selectedDonorId);
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
                        donatedStmt.setInt(1, selectedDonorId);
                        ResultSet donatedRS = donatedStmt.executeQuery();

                        if (donatedRS.next()) {
                            Date donatedDateSql = donatedRS.getDate("donated_date");
                            if (donatedDateSql != null) {
                                lastDonationDate = donatedDateSql.toLocalDate();
                            }
                        }
                    }

                    // Step 3: Check 56-day eligibility rule for the donor
                    if (lastDonationDate != null) {
                        LocalDate nextEligibleDate = lastDonationDate.plusDays(56);
                        if (appointmentDate.isBefore(nextEligibleDate)) {
                            statusLabel.setText("Donor not eligible. Next eligible date: " + nextEligibleDate);
                            return;
                        }
                    }

                    // Step 4: Insert appointment for the donor in the selected Blood Bank
                    String insertSQL = "INSERT INTO Appointment (date, D_id, BB_id) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(insertSQL);
                    stmt.setString(1, dateTimeStr);
                    stmt.setInt(2, selectedDonorId);
                    stmt.setInt(3, bloodBankId);

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

        // Back to Dashboard button
        gbc.gridy++;
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard"));
        panel.add(backButton, gbc);

        return panel;
    }

    
    /*private JPanel createBloodBankAppointmentListPanel(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Blood Bank Appointment List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table model and table
        String[] columnNames = { "Appointment ID", "Date & Time", "Donor Name", "Phone", "Email" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable appointmentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch appointment data from the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            String query =  "SELECT A.appointment_id, A.date, D.first_name, D.last_name, D.ph_no, D.email " +
                    "FROM Appointment A " +
                    "JOIN Donor D ON A.D_id = D.D_id " +
                    "WHERE A.BB_id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bloodBankId);
            ResultSet rs = stmt.executeQuery();

            // Add data to table model
            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                String dateTime = rs.getString("date");
                String donorName = rs.getString("first_name") + " " + rs.getString("last_name");
                String phone = rs.getString("ph_no");
                String email = rs.getString("email");

                model.addRow(new Object[] { appointmentId, dateTime, donorName, phone, email });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error loading appointment list.");
        }

        // Back to Dashboard button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
*/
    private JPanel createBloodStockPanel(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Blood Stock", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "Blood Type", "Quantity (ml)", "Expiry Date" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable stockTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            // Debugging: Show the BB_id being used
            System.out.println("BB_id being used: " + bloodBankId);

            // Query all data to test if any data is fetched
            String query = "SELECT * FROM BloodStock WHERE BB_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bloodBankId);

            ResultSet rs = stmt.executeQuery();

            boolean hasData = false;

            // Debugging: Checking the columns returned
            System.out.println("ResultSet Columns: " + rs.getMetaData().getColumnCount());
            System.out.println("ResultSet Column Names: " + rs.getMetaData().getColumnName(1) + ", " + rs.getMetaData().getColumnName(2) + ", " + rs.getMetaData().getColumnName(3));

            while (rs.next()) {
                System.out.println("Fetching row...");
                String bloodType = rs.getString("bloodtype");
                int quantity = rs.getInt("quantity");
                Date expiryDate = rs.getDate("expiry_date");

                // Debugging: Print the values being retrieved
                System.out.println("Row: " + bloodType + ", " + quantity + ", " + expiryDate);

                model.addRow(new Object[] { bloodType, quantity, expiryDate });
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

    private JPanel createRequestedBloodPanel(int bloodBankId) {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Requested Blood", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = { "Transaction ID", "Blood Type", "Quantity (ml)", "Status", "Request Date" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable transactionsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch request data from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            System.out.println("Connected to database successfully.");

            // SQL Query: Use 'BloodBank_id' if that's the correct column
            String query = "SELECT t.T_id, br.bloodtype, br.quantity, br.status, br.request_date " +
                           "FROM Transaction t " +
                           "JOIN BloodRequest br ON t.BR_id = br.BR_id " +
                           "WHERE t.BloodBank_id = ?";
            System.out.println("SQL Query: " + query);
            System.out.println("Blood Bank ID: " + bloodBankId);

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bloodBankId);

            ResultSet rs = stmt.executeQuery();

            // Check if any data is returned
            boolean hasData = false;

            while (rs.next()) {
                int transactionId = rs.getInt("T_id");
                String bloodType = rs.getString("bloodtype");  // Correct column 'bloodtype'
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");
                String requestDate = rs.getString("request_date");

                // Add the row to the table model
                model.addRow(new Object[] { transactionId, bloodType, quantity, status, requestDate });
                hasData = true;
            }

            // If no data found, notify user
            if (!hasData) {
                JOptionPane.showMessageDialog(panel, "No blood request records found for this blood bank.");
            }

        } catch (Exception e) {
            // Print full exception stack trace for debugging
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error loading blood requests: " + e.getMessage());
        }

        // Back to dashboard
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "BloodBankDashboard"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }


    

       // Helper to create text fields
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

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            String sql = "SELECT * FROM login WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int loginId = rs.getInt("id");
                int donorId = rs.getInt("id");
                int hospitalId = rs.getInt("id");
                int bloodbankId=rs.getInt("id");
				this.loginId = loginId;
                if(role.equalsIgnoreCase("donor")){
                	String namequery="SELECT first_name,last_name from donor where login_id = ?";
                	PreparedStatement namestmt = conn.prepareStatement(namequery); 
                	namestmt.setInt(1, loginId);
                	ResultSet nameRs = namestmt.executeQuery();
                	if(nameRs.next()) {
                		String firstname = nameRs.getString("first_name");
                		String lastname = nameRs.getString("last_name");
                		welcomeLabel.setText("WELCOME, "+ firstname + lastname );
                	}
                	mainPanel.add(createDonorProfilePanel(loginId), "DonorProfile");
                	mainPanel.add(createAppointmentBookingPanel(donorId), "BookAppointment");
                	mainPanel.add(createDonationHistoryPanel(donorId), "DonationHistory");
                	mainPanel.add(createReceiveNotificationPanel(donorId), "Notifications");
                	mainPanel.add(createAppointmentHistoryPanel(donorId), "AppointmentHistory");
                	cardLayout.show(mainPanel,"DonorDashboard");
                }
               
                else if (role.equalsIgnoreCase("hospital")) 
                {
                    String nameQuery = "SELECT name FROM hospitalregister WHERE Hlogin_id = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(nameQuery);
                    stmt2.setInt(1, loginId);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        welcomeLabel.setText("WELCOME, " + rs2.getString("name"));
                    }
                    mainPanel.add(createHospitalProfilePanel(hospitalId),"HospitalProfile");
                    mainPanel.add(createBloodRequestPanel(hospitalId),"BloodRequest");
                    mainPanel.add(createHospitalNotificationPanel(hospitalId),"Notifications");
                    mainPanel.add(createHospitalRequestHistoryPanel(hospitalId),"viewbloodRequestHistory");
                    mainPanel.add(createHospitalDashboardPanel(loginId), "HospitalDashboard");
                    cardLayout.show(mainPanel, "HospitalDashboard");
                }
                else if (role.equalsIgnoreCase("bloodbank")) {
                    String nameQuery = "SELECT name FROM bloodbank WHERE BBlogin_id = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(nameQuery);
                    stmt2.setInt(1, loginId);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        welcomeLabel.setText("WELCOME, " + rs2.getString("name"));
                    }
                    mainPanel.add(createBloodBankProfilePanel(bloodbankId),"BloodBankProfile");
                    mainPanel.add(createBloodBankAppointmentListPanel(bloodbankId),"BloodBankAppointmentList");
                    //mainPanel.add(createHospitalNotificationPanel(hospitalId),"Notifications");
                    mainPanel.add( createBloodStockPanel(bloodbankId),"BloodStock");
                    mainPanel.add(createRequestedBloodPanel(bloodbankId), "RequestedBlood");
                    mainPanel.add(createBloodBankDashboard(loginId), "BloodBankDashboard");

                    cardLayout.show(mainPanel, "BloodBankDashboard");
                }
            else {
                JOptionPane.showMessageDialog(this, "Login successful! Role: " + role);
                
                loggedInUserId = rs.getInt("id");
        
                }
                
            } 
            
            else {
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
        }// Can be made user-defined

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "rashmiMali@864;")) {
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Insert into login
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

            if (loginId == -1) t