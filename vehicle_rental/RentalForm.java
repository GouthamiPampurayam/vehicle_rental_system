import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class RentalForm extends JFrame {
    private JTextField txtRentalId;
    private JComboBox<CustomerComboItem> comboCustomer;
    private JComboBox<VehicleComboItem> comboVehicle;
    private JTextField txtDays;
    private JTextField txtRentDate;
    private JTextField txtReturnDate;
    private JTextField txtTotalAmount;
    private JTextField txtStatus;
    
    // Dynamic status helper label to guide the user visually
    private JLabel lblStatusInfo;

    private JButton btnCalculateBill, btnRent, btnReturn, btnClear, btnRefresh;
    private JTable tableRentals;
    private DefaultTableModel tableModel;

    private RentalDAO rentalDAO;
    private CustomerDAO customerDAO;
    private VehicleDAO vehicleDAO;

    public RentalForm() {
        rentalDAO = new RentalDAO();
        customerDAO = new CustomerDAO();
        vehicleDAO = new VehicleDAO();

        setTitle("Vehicle Rental System - Rental Management");
        setSize(1150, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        
        loadCustomers();
        loadAvailableVehicles();
        refreshRentalsTable();
        clearFields();
    }

    private void initComponents() {
        // Theme Colors
        Color bgDark = new Color(15, 23, 42);       // Tailwind slate-900 (Deep dark background)
        Color bgCard = new Color(30, 41, 59);       // Tailwind slate-800 (Card background)
        Color bgInputReadOnly = new Color(22, 28, 45); // Deep slate for read-only fields
        Color textLight = new Color(248, 250, 252); // Tailwind slate-50 (White text)
        Color textMuted = new Color(148, 163, 184); // Tailwind slate-400 (Muted/Read-only text)
        Color borderSlate = new Color(51, 65, 85);  // Tailwind slate-700
        Color accentBlue = new Color(14, 165, 233); // Sky-500

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(bgDark);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel (Top)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgCard);
        headerPanel.setPreferredSize(new Dimension(1150, 65));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        JLabel lblTitle = new JLabel("RENTAL MANAGEMENT PORTAL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(accentBlue);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(accentBlue);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());
        headerPanel.add(btnBack, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Left Input Panel (Card style)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgCard);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Dynamic State / Action Indicator
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        lblStatusInfo = new JLabel("Mode: Create New Rental");
        lblStatusInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatusInfo.setForeground(new Color(46, 204, 113)); // Green mode
        lblStatusInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        formPanel.add(lblStatusInfo, gbc);
        gbc.gridwidth = 1; // Reset

        // Rental ID
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblRentalId = new JLabel("Rental ID:");
        lblRentalId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRentalId.setForeground(textLight);
        formPanel.add(lblRentalId, gbc);

        gbc.gridx = 1;
        txtRentalId = new JTextField();
        txtRentalId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRentalId.setEditable(false);
        txtRentalId.setForeground(textMuted);
        txtRentalId.setBackground(bgInputReadOnly);
        txtRentalId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtRentalId, gbc);

        // Customer Selection
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblCust = new JLabel("Customer:");
        lblCust.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCust.setForeground(textLight);
        formPanel.add(lblCust, gbc);

        gbc.gridx = 1;
        comboCustomer = new JComboBox<>();
        comboCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboCustomer.setForeground(textLight);
        comboCustomer.setBackground(bgDark);
        formPanel.add(comboCustomer, gbc);

        // Vehicle Selection
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblVeh = new JLabel("Vehicle:");
        lblVeh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblVeh.setForeground(textLight);
        formPanel.add(lblVeh, gbc);

        gbc.gridx = 1;
        comboVehicle = new JComboBox<>();
        comboVehicle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboVehicle.setForeground(textLight);
        comboVehicle.setBackground(bgDark);
        formPanel.add(comboVehicle, gbc);

        // Rental Days
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblDays = new JLabel("Rental Days:");
        lblDays.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDays.setForeground(textLight);
        formPanel.add(lblDays, gbc);

        gbc.gridx = 1;
        txtDays = new JTextField();
        txtDays.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDays.setForeground(textLight);
        txtDays.setBackground(bgDark);
        txtDays.setCaretColor(textLight);
        txtDays.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtDays, gbc);

        // Rent Date
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblRentDate = new JLabel("Rent Date:");
        lblRentDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRentDate.setForeground(textLight);
        formPanel.add(lblRentDate, gbc);

        gbc.gridx = 1;
        txtRentDate = new JTextField();
        txtRentDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRentDate.setEditable(false);
        txtRentDate.setForeground(textMuted);
        txtRentDate.setBackground(bgInputReadOnly);
        txtRentDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtRentDate, gbc);

        // Return Date
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblReturnDate = new JLabel("Return Date:");
        lblReturnDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblReturnDate.setForeground(textLight);
        formPanel.add(lblReturnDate, gbc);

        gbc.gridx = 1;
        txtReturnDate = new JTextField();
        txtReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtReturnDate.setEditable(false);
        txtReturnDate.setForeground(textMuted);
        txtReturnDate.setBackground(bgInputReadOnly);
        txtReturnDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtReturnDate, gbc);

        // Total Amount
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblAmount = new JLabel("Total Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAmount.setForeground(textLight);
        formPanel.add(lblAmount, gbc);

        gbc.gridx = 1;
        txtTotalAmount = new JTextField();
        txtTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTotalAmount.setEditable(false);
        txtTotalAmount.setForeground(textMuted);
        txtTotalAmount.setBackground(bgInputReadOnly);
        txtTotalAmount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtTotalAmount, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatus.setForeground(textLight);
        formPanel.add(lblStatus, gbc);

        gbc.gridx = 1;
        txtStatus = new JTextField();
        txtStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtStatus.setEditable(false);
        txtStatus.setForeground(textMuted);
        txtStatus.setBackground(bgInputReadOnly);
        txtStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtStatus, gbc);

        // Actions Panel (Buttons)
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        buttonsPanel.setOpaque(false);

        btnCalculateBill = createFormButton("Calculate Bill", new Color(139, 92, 246));
        btnRent = createFormButton("Rent Vehicle", new Color(16, 185, 129));
        btnReturn = createFormButton("Return Vehicle", new Color(245, 158, 11));
        btnClear = createFormButton("Clear / New", new Color(100, 116, 139));
        btnRefresh = createFormButton("Refresh Lists", new Color(14, 165, 233));

        buttonsPanel.add(btnCalculateBill);
        buttonsPanel.add(btnRent);
        buttonsPanel.add(btnReturn);
        buttonsPanel.add(btnClear);
        buttonsPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 8, 8, 8);
        formPanel.add(buttonsPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        // Right Table Panel (Logs view)
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(bgCard);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Instructions header for table
        JLabel lblTableTitle = new JLabel(" Rental Transaction Records");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTableTitle.setForeground(accentBlue);
        lblTableTitle.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, accentBlue));
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);

        String[] columnNames = {"Rental ID", "Customer ID", "Vehicle ID", "Rent Date", "Return Date", "Days", "Amount ($)", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableRentals = new JTable(tableModel);
        tableRentals.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableRentals.setRowHeight(25);
        tableRentals.setBackground(bgCard);
        tableRentals.setForeground(textLight);
        tableRentals.setGridColor(borderSlate);
        tableRentals.setSelectionBackground(new Color(14, 165, 233, 50));
        tableRentals.setSelectionForeground(Color.WHITE);

        // Header Styling
        tableRentals.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableRentals.getTableHeader().setBackground(bgDark);
        tableRentals.getTableHeader().setForeground(textLight);
        tableRentals.getTableHeader().setReorderingAllowed(false);

        // Center align table contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableRentals.getColumnCount(); i++) {
            tableRentals.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableRentals);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderSlate));
        scrollPane.getViewport().setBackground(bgCard);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        // Setup action listeners
        btnCalculateBill.addActionListener(e -> calculateRentAmount());
        btnRent.addActionListener(e -> rentVehicle());
        btnReturn.addActionListener(e -> returnVehicle());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> {
            loadCustomers();
            loadAvailableVehicles();
            refreshRentalsTable();
        });

        tableRentals.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableRentals.getSelectedRow();
                if (selectedRow != -1) {
                    txtRentalId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    
                    // Match customer dropdown
                    int customerId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                    selectCustomerInCombo(customerId);

                    // Match vehicle dropdown
                    int vehicleId = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());
                    selectVehicleInCombo(vehicleId);

                    txtRentDate.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    
                    Object returnVal = tableModel.getValueAt(selectedRow, 4);
                    txtReturnDate.setText(returnVal != null ? returnVal.toString() : "");

                    txtDays.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    txtTotalAmount.setText(tableModel.getValueAt(selectedRow, 6).toString());
                    
                    String status = tableModel.getValueAt(selectedRow, 7).toString();
                    txtStatus.setText(status);

                    // Adjust button states & guidance details dynamically
                    if ("Returned".equals(status)) {
                        lblStatusInfo.setText("Mode: Viewing Finalized Transaction");
                        lblStatusInfo.setForeground(new Color(149, 165, 166)); // Gray
                        btnReturn.setEnabled(false);
                        btnRent.setEnabled(false);
                    } else {
                        lblStatusInfo.setText("Mode: Process Return Transaction");
                        lblStatusInfo.setForeground(new Color(245, 158, 11)); // Amber
                        btnReturn.setEnabled(true);
                        btnRent.setEnabled(false);
                    }
                }
            }
        });
    }

    private JButton createFormButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void resetDates() {
        txtRentDate.setText(new Date(System.currentTimeMillis()).toString());
        txtReturnDate.setText("");
    }

    private void loadCustomers() {
        try {
            comboCustomer.removeAllItems();
            List<Customer> list = customerDAO.getAllCustomers();
            for (Customer c : list) {
                comboCustomer.addItem(new CustomerComboItem(c.getCustomerId(), c.getCustomerName()));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAvailableVehicles() {
        try {
            comboVehicle.removeAllItems();
            List<Vehicle> list = vehicleDAO.getAvailableVehicles();
            for (Vehicle v : list) {
                comboVehicle.addItem(new VehicleComboItem(v.getVehicleId(), v.getVehicleName(), v.getVehicleType(), v.getRentPerDay()));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading available vehicles: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectCustomerInCombo(int customerId) {
        for (int i = 0; i < comboCustomer.getItemCount(); i++) {
            if (comboCustomer.getItemAt(i).id == customerId) {
                comboCustomer.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectVehicleInCombo(int vehicleId) {
        for (int i = 0; i < comboVehicle.getItemCount(); i++) {
            if (comboVehicle.getItemAt(i).id == vehicleId) {
                comboVehicle.setSelectedIndex(i);
                return;
            }
        }
        
        // If the vehicle is currently rented and NOT in the available combo items, load it temporarily
        try {
            Vehicle v = vehicleDAO.searchVehicle(vehicleId);
            if (v != null) {
                VehicleComboItem item = new VehicleComboItem(v.getVehicleId(), v.getVehicleName(), v.getVehicleType(), v.getRentPerDay());
                comboVehicle.insertItemAt(item, 0);
                comboVehicle.setSelectedIndex(0);
            }
        } catch (SQLException ex) {
            // Ignore
        }
    }

    private void refreshRentalsTable() {
        try {
            tableModel.setRowCount(0);
            List<Rental> list = rentalDAO.getAllRentals();
            for (Rental r : list) {
                tableModel.addRow(new Object[]{
                        r.getRentalId(),
                        r.getCustomerId(),
                        r.getVehicleId(),
                        r.getRentDate(),
                        r.getReturnDate(),
                        r.getDays(),
                        r.getTotalAmount(),
                        r.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching rental logs: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtRentalId.setText("");
        if (comboCustomer.getItemCount() > 0) comboCustomer.setSelectedIndex(0);
        if (comboVehicle.getItemCount() > 0) comboVehicle.setSelectedIndex(0);
        txtDays.setText("");
        txtTotalAmount.setText("");
        txtStatus.setText("New");
        resetDates();
        
        // Reset guidance mode and button states
        lblStatusInfo.setText("Mode: Create New Rental");
        lblStatusInfo.setForeground(new Color(46, 204, 113)); // Green
        btnRent.setEnabled(true);
        btnReturn.setEnabled(false);
        tableRentals.clearSelection();
    }

    private double calculateRentAmount() {
        try {
            VehicleComboItem selectedVehicleItem = (VehicleComboItem) comboVehicle.getSelectedItem();
            String daysStr = txtDays.getText().trim();

            if (selectedVehicleItem == null) {
                throw new IllegalArgumentException("Please select a vehicle from the list.");
            }
            if (daysStr.isEmpty()) {
                throw new IllegalArgumentException("Please enter rental days.");
            }

            int days = Integer.parseInt(daysStr);
            if (days <= 0) {
                throw new IllegalArgumentException("Rental days must be greater than zero.");
            }

            double total = rentalDAO.calculateBill(selectedVehicleItem.id, days);
            txtTotalAmount.setText(String.format("%.2f", total));
            return total;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Days must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    private void rentVehicle() {
        try {
            CustomerComboItem selectedCust = (CustomerComboItem) comboCustomer.getSelectedItem();
            VehicleComboItem selectedVeh = (VehicleComboItem) comboVehicle.getSelectedItem();
            String daysStr = txtDays.getText().trim();

            if (selectedCust == null) {
                throw new IllegalArgumentException("Please register a customer first or select one.");
            }
            if (selectedVeh == null) {
                throw new IllegalArgumentException("Please add an available vehicle first or select one.");
            }
            if (daysStr.isEmpty()) {
                throw new IllegalArgumentException("Please enter the number of rental days.");
            }

            int days = Integer.parseInt(daysStr);
            if (days <= 0) {
                throw new IllegalArgumentException("Rental days must be greater than zero.");
            }

            // Verify the vehicle is indeed available in DB before proceeding
            Vehicle actualVeh = vehicleDAO.searchVehicle(selectedVeh.id);
            if (actualVeh == null || !"Yes".equalsIgnoreCase(actualVeh.getAvailable())) {
                throw new IllegalArgumentException("The selected vehicle is no longer available.");
            }

            double totalAmount = calculateRentAmount();
            if (totalAmount <= 0) return; // calculation failed

            Date rentDate = new Date(System.currentTimeMillis());

            // Compute return date: rentDate + days
            Calendar c = Calendar.getInstance();
            c.setTime(rentDate);
            c.add(Calendar.DATE, days);
            Date returnDate = new Date(c.getTimeInMillis());

            Rental rental = new Rental(0, selectedCust.id, selectedVeh.id, rentDate, returnDate, days, totalAmount, "Rented");

            // Execute rent in database
            if (rentalDAO.rentVehicle(rental)) {
                JOptionPane.showMessageDialog(this, "Vehicle rented successfully!\nTotal Bill: $" + String.format("%.2f", totalAmount), "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadAvailableVehicles();
                refreshRentalsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Rent vehicle transaction failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Days must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnVehicle() {
        try {
            int rentalId = -1;
            String rentalIdStr = txtRentalId.getText().trim();
            
            if (!rentalIdStr.isEmpty()) {
                rentalId = Integer.parseInt(rentalIdStr);
            } else {
                // Try to find the active rental by selected customer and vehicle
                CustomerComboItem selectedCust = (CustomerComboItem) comboCustomer.getSelectedItem();
                VehicleComboItem selectedVeh = (VehicleComboItem) comboVehicle.getSelectedItem();
                
                if (selectedCust == null) {
                    throw new IllegalArgumentException("Please select a customer first.");
                }
                if (selectedVeh == null) {
                    throw new IllegalArgumentException("Please select a vehicle first.");
                }
                
                // Fetch the active rental ID
                rentalId = rentalDAO.getActiveRentalId(selectedCust.id, selectedVeh.id);
            }

            if (rentalId == -1) {
                throw new IllegalArgumentException("No active 'Rented' transaction was found for the selected customer and vehicle.");
            }
            
            // Confirm return
            int confirm = JOptionPane.showConfirmDialog(this, "Confirm return of vehicle for Rental ID " + rentalId + "?", "Confirm Return", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Date returnDate = new Date(System.currentTimeMillis());
                if (rentalDAO.returnVehicle(rentalId, returnDate)) {
                    JOptionPane.showMessageDialog(this, "Vehicle returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    loadAvailableVehicles();
                    refreshRentalsTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to complete return transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper classes for JComboBox items
    private static class CustomerComboItem {
        int id;
        String name;

        public CustomerComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id + " : " + name;
        }
    }

    private static class VehicleComboItem {
        int id;
        String name;
        String type;
        double rentPerDay;

        public VehicleComboItem(int id, String name, String type, double rentPerDay) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.rentPerDay = rentPerDay;
        }

        @Override
        public String toString() {
            return id + " : " + name + " (" + type + " - $" + rentPerDay + "/day)";
        }
    }
}
