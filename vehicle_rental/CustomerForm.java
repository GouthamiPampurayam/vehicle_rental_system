import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class CustomerForm extends JFrame {
    private JTextField txtCustomerId;
    private JTextField txtCustomerName;
    private JTextField txtPhone;
    private JTextField txtLicenseNumber;

    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnClear, btnRefresh;
    private JTable tableCustomers;
    private DefaultTableModel tableModel;

    private CustomerDAO customerDAO;

    public CustomerForm() {
        customerDAO = new CustomerDAO();
        setTitle("Vehicle Rental System - Customer Management");
        setSize(980, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Theme Colors
        Color bgDark = new Color(15, 23, 42);       // Tailwind slate-900 (Deep dark background)
        Color bgCard = new Color(30, 41, 59);       // Tailwind slate-800 (Card background)
        Color textLight = new Color(248, 250, 252); // Tailwind slate-50 (White text)
        Color borderSlate = new Color(51, 65, 85);  // Tailwind slate-700
        Color accentBlue = new Color(14, 165, 233); // Sky-500

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(bgDark);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel (Top)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgCard);
        headerPanel.setPreferredSize(new Dimension(980, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("CUSTOMER MANAGEMENT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(accentBlue);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(accentBlue);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());
        headerPanel.add(btnBack, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Left Form Panel (Card container)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgCard);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("Customer ID:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblId.setForeground(textLight);
        formPanel.add(lblId, gbc);

        gbc.gridx = 1;
        txtCustomerId = new JTextField(12);
        txtCustomerId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCustomerId.setForeground(textLight);
        txtCustomerId.setBackground(bgDark);
        txtCustomerId.setCaretColor(textLight);
        txtCustomerId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtCustomerId.setToolTipText("Auto-generated for Add. Enter for Search/Update/Delete.");
        formPanel.add(txtCustomerId, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblName = new JLabel("Full Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblName.setForeground(textLight);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        txtCustomerName = new JTextField(12);
        txtCustomerName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCustomerName.setForeground(textLight);
        txtCustomerName.setBackground(bgDark);
        txtCustomerName.setCaretColor(textLight);
        txtCustomerName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtCustomerName, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPhone.setForeground(textLight);
        formPanel.add(lblPhone, gbc);

        gbc.gridx = 1;
        txtPhone = new JTextField(12);
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPhone.setForeground(textLight);
        txtPhone.setBackground(bgDark);
        txtPhone.setCaretColor(textLight);
        txtPhone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtPhone, gbc);

        // License Number
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblLicense = new JLabel("License No:");
        lblLicense.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLicense.setForeground(textLight);
        formPanel.add(lblLicense, gbc);

        gbc.gridx = 1;
        txtLicenseNumber = new JTextField(12);
        txtLicenseNumber.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtLicenseNumber.setForeground(textLight);
        txtLicenseNumber.setBackground(bgDark);
        txtLicenseNumber.setCaretColor(textLight);
        txtLicenseNumber.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtLicenseNumber, gbc);

        // Actions Panel (Buttons)
        JPanel actionButtonPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        actionButtonPanel.setOpaque(false);

        btnAdd = createFormButton("Add", new Color(16, 185, 129));
        btnUpdate = createFormButton("Update", new Color(245, 158, 11));
        btnDelete = createFormButton("Delete", new Color(239, 68, 68));
        btnSearch = createFormButton("Search", new Color(139, 92, 246));
        btnClear = createFormButton("Clear", new Color(100, 116, 139));
        btnRefresh = createFormButton("Refresh", new Color(14, 165, 233));

        actionButtonPanel.add(btnAdd);
        actionButtonPanel.add(btnUpdate);
        actionButtonPanel.add(btnDelete);
        actionButtonPanel.add(btnSearch);
        actionButtonPanel.add(btnClear);
        actionButtonPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 8, 8, 8);
        formPanel.add(actionButtonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        // Table Panel (Right side)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(bgCard);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"Customer ID", "Customer Name", "Phone", "License Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCustomers = new JTable(tableModel);
        tableCustomers.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableCustomers.setRowHeight(25);
        tableCustomers.setBackground(bgCard);
        tableCustomers.setForeground(textLight);
        tableCustomers.setGridColor(borderSlate);
        tableCustomers.setSelectionBackground(new Color(14, 165, 233, 50));
        tableCustomers.setSelectionForeground(Color.WHITE);

        // Header Style
        tableCustomers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableCustomers.getTableHeader().setBackground(bgDark);
        tableCustomers.getTableHeader().setForeground(textLight);
        tableCustomers.getTableHeader().setReorderingAllowed(false);

        // Center align table contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableCustomers.getColumnCount(); i++) {
            tableCustomers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableCustomers);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderSlate));
        scrollPane.getViewport().setBackground(bgCard); // Make viewport match table background
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        // Event Listeners
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnSearch.addActionListener(e -> searchCustomer());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> refreshTable());

        tableCustomers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableCustomers.getSelectedRow();
                if (selectedRow != -1) {
                    txtCustomerId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtCustomerName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtPhone.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtLicenseNumber.setText(tableModel.getValueAt(selectedRow, 3).toString());
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

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Customer> list = customerDAO.getAllCustomers();
            for (Customer c : list) {
                tableModel.addRow(new Object[]{
                        c.getCustomerId(),
                        c.getCustomerName(),
                        c.getPhone(),
                        c.getLicenseNumber()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching customers: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtPhone.setText("");
        txtLicenseNumber.setText("");
        tableCustomers.clearSelection();
    }

    private void addCustomer() {
        try {
            String name = txtCustomerName.getText().trim();
            String phone = txtPhone.getText().trim();
            String license = txtLicenseNumber.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || license.isEmpty()) {
                throw new IllegalArgumentException("All fields (Name, Phone, License) are required to add.");
            }

            Customer customer = new Customer(0, name, phone, license);
            if (customerDAO.addCustomer(customer)) {
                JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        try {
            String idStr = txtCustomerId.getText().trim();
            String name = txtCustomerName.getText().trim();
            String phone = txtPhone.getText().trim();
            String license = txtLicenseNumber.getText().trim();

            if (idStr.isEmpty() || name.isEmpty() || phone.isEmpty() || license.isEmpty()) {
                throw new IllegalArgumentException("All fields, including Customer ID, are required for update.");
            }

            int id = Integer.parseInt(idStr);

            Customer customer = new Customer(id, name, phone, license);
            if (customerDAO.updateCustomer(customer)) {
                JOptionPane.showMessageDialog(this, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found or details unchanged.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Customer ID must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        try {
            String idStr = txtCustomerId.getText().trim();
            if (idStr.isEmpty()) {
                throw new IllegalArgumentException("Please select or enter a Customer ID to delete.");
            }

            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete customer ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (customerDAO.deleteCustomer(id)) {
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Customer ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Customer ID must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error (check associated rental records): " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchCustomer() {
        try {
            String idStr = txtCustomerId.getText().trim();
            String name = txtCustomerName.getText().trim();

            if (idStr.isEmpty() && name.isEmpty()) {
                throw new IllegalArgumentException("Please enter a Customer ID or Full Name to search.");
            }

            if (!idStr.isEmpty()) {
                // Search by ID
                int id = Integer.parseInt(idStr);
                Customer c = customerDAO.searchCustomer(id);
                if (c != null) {
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[]{
                            c.getCustomerId(),
                            c.getCustomerName(),
                            c.getPhone(),
                            c.getLicenseNumber()
                    });

                    // Set fields
                    txtCustomerName.setText(c.getCustomerName());
                    txtPhone.setText(c.getPhone());
                    txtLicenseNumber.setText(c.getLicenseNumber());
                } else {
                    JOptionPane.showMessageDialog(this, "Customer not found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Search by Name
                List<Customer> list = customerDAO.searchCustomersByName(name);
                if (!list.isEmpty()) {
                    tableModel.setRowCount(0);
                    for (Customer c : list) {
                        tableModel.addRow(new Object[]{
                                c.getCustomerId(),
                                c.getCustomerName(),
                                c.getPhone(),
                                c.getLicenseNumber()
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No customers found matching: " + name, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Customer ID must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
