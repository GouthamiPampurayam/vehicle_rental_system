import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class VehicleForm extends JFrame {
    private JTextField txtVehicleId;
    private JTextField txtVehicleName;
    private JComboBox<String> comboVehicleType;
    private JTextField txtRentPerDay;
    private JComboBox<String> comboAvailable;

    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnClear, btnRefresh;
    private JTable tableVehicles;
    private DefaultTableModel tableModel;

    private VehicleDAO vehicleDAO;

    public VehicleForm() {
        vehicleDAO = new VehicleDAO();
        setTitle("Vehicle Rental System - Vehicle Management");
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

        JLabel lblTitle = new JLabel("VEHICLE MANAGEMENT");
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

        // Vehicle ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("Vehicle ID:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblId.setForeground(textLight);
        formPanel.add(lblId, gbc);

        gbc.gridx = 1;
        txtVehicleId = new JTextField(12);
        txtVehicleId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtVehicleId.setForeground(textLight);
        txtVehicleId.setBackground(bgDark);
        txtVehicleId.setCaretColor(textLight);
        txtVehicleId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtVehicleId.setToolTipText("Auto-generated for Add. Enter for Search/Update/Delete.");
        formPanel.add(txtVehicleId, gbc);

        // Vehicle Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblName = new JLabel("Vehicle Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblName.setForeground(textLight);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        txtVehicleName = new JTextField(12);
        txtVehicleName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtVehicleName.setForeground(textLight);
        txtVehicleName.setBackground(bgDark);
        txtVehicleName.setCaretColor(textLight);
        txtVehicleName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtVehicleName, gbc);

        // Vehicle Type
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblType = new JLabel("Vehicle Type:");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblType.setForeground(textLight);
        formPanel.add(lblType, gbc);

        gbc.gridx = 1;
        comboVehicleType = new JComboBox<>(new String[]{"Car", "Bike"});
        comboVehicleType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboVehicleType.setForeground(textLight);
        comboVehicleType.setBackground(bgDark);
        formPanel.add(comboVehicleType, gbc);

        // Rent Per Day
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblRent = new JLabel("Rent / Day ($):");
        lblRent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRent.setForeground(textLight);
        formPanel.add(lblRent, gbc);

        gbc.gridx = 1;
        txtRentPerDay = new JTextField(12);
        txtRentPerDay.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRentPerDay.setForeground(textLight);
        txtRentPerDay.setBackground(bgDark);
        txtRentPerDay.setCaretColor(textLight);
        txtRentPerDay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtRentPerDay, gbc);

        // Available
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblAvailable = new JLabel("Available:");
        lblAvailable.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAvailable.setForeground(textLight);
        formPanel.add(lblAvailable, gbc);

        gbc.gridx = 1;
        comboAvailable = new JComboBox<>(new String[]{"Yes", "No"});
        comboAvailable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboAvailable.setForeground(textLight);
        comboAvailable.setBackground(bgDark);
        formPanel.add(comboAvailable, gbc);

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

        gbc.gridx = 0; gbc.gridy = 5;
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

        String[] columnNames = {"Vehicle ID", "Vehicle Name", "Type", "Rent / Day", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableVehicles = new JTable(tableModel);
        tableVehicles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableVehicles.setRowHeight(25);
        tableVehicles.setBackground(bgCard);
        tableVehicles.setForeground(textLight);
        tableVehicles.setGridColor(borderSlate);
        tableVehicles.setSelectionBackground(new Color(14, 165, 233, 50));
        tableVehicles.setSelectionForeground(Color.WHITE);

        // Header Style
        tableVehicles.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableVehicles.getTableHeader().setBackground(bgDark);
        tableVehicles.getTableHeader().setForeground(textLight);
        tableVehicles.getTableHeader().setReorderingAllowed(false);

        // Center align table contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableVehicles.getColumnCount(); i++) {
            tableVehicles.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableVehicles);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderSlate));
        scrollPane.getViewport().setBackground(bgCard); // Make viewport match table background
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        // Event Listeners
        btnAdd.addActionListener(e -> addVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
        btnSearch.addActionListener(e -> searchVehicle());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> refreshTable());

        tableVehicles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableVehicles.getSelectedRow();
                if (selectedRow != -1) {
                    txtVehicleId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtVehicleName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    comboVehicleType.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                    txtRentPerDay.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    comboAvailable.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
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
            List<Vehicle> list = vehicleDAO.getAllVehicles();
            for (Vehicle v : list) {
                tableModel.addRow(new Object[]{
                        v.getVehicleId(),
                        v.getVehicleName(),
                        v.getVehicleType(),
                        v.getRentPerDay(),
                        v.getAvailable()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching vehicles: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtVehicleId.setText("");
        txtVehicleName.setText("");
        comboVehicleType.setSelectedIndex(0);
        txtRentPerDay.setText("");
        comboAvailable.setSelectedIndex(0);
        tableVehicles.clearSelection();
    }

    private void addVehicle() {
        try {
            String name = txtVehicleName.getText().trim();
            String type = (String) comboVehicleType.getSelectedItem();
            String rentStr = txtRentPerDay.getText().trim();
            String available = (String) comboAvailable.getSelectedItem();

            if (name.isEmpty() || rentStr.isEmpty()) {
                throw new IllegalArgumentException("Fields cannot be empty.");
            }

            double rentPerDay = Double.parseDouble(rentStr);
            if (rentPerDay < 0) {
                throw new IllegalArgumentException("Rent per day cannot be negative.");
            }

            Vehicle vehicle;
            if ("Car".equalsIgnoreCase(type)) {
                vehicle = new Car(0, name, rentPerDay, available);
            } else {
                vehicle = new Bike(0, name, rentPerDay, available);
            }

            if (vehicleDAO.addVehicle(vehicle)) {
                JOptionPane.showMessageDialog(this, "Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rent per day must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        try {
            String idStr = txtVehicleId.getText().trim();
            String name = txtVehicleName.getText().trim();
            String type = (String) comboVehicleType.getSelectedItem();
            String rentStr = txtRentPerDay.getText().trim();
            String available = (String) comboAvailable.getSelectedItem();

            if (idStr.isEmpty() || name.isEmpty() || rentStr.isEmpty()) {
                throw new IllegalArgumentException("All fields, including Vehicle ID, are required for update.");
            }

            int id = Integer.parseInt(idStr);
            double rentPerDay = Double.parseDouble(rentStr);
            if (rentPerDay < 0) {
                throw new IllegalArgumentException("Rent per day cannot be negative.");
            }

            Vehicle vehicle;
            if ("Car".equalsIgnoreCase(type)) {
                vehicle = new Car(id, name, rentPerDay, available);
            } else {
                vehicle = new Bike(id, name, rentPerDay, available);
            }

            if (vehicleDAO.updateVehicle(vehicle)) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle ID not found or details unchanged.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vehicle ID and Rent per day must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        try {
            String idStr = txtVehicleId.getText().trim();
            if (idStr.isEmpty()) {
                throw new IllegalArgumentException("Please select or enter a Vehicle ID to delete.");
            }

            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete vehicle ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (vehicleDAO.deleteVehicle(id)) {
                    JOptionPane.showMessageDialog(this, "Vehicle deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Vehicle ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vehicle ID must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error (check foreign key constraints): " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchVehicle() {
        try {
            String idStr = txtVehicleId.getText().trim();
            String name = txtVehicleName.getText().trim();

            if (idStr.isEmpty() && name.isEmpty()) {
                throw new IllegalArgumentException("Please enter a Vehicle ID or Name to search.");
            }

            if (!idStr.isEmpty()) {
                // Search by ID
                int id = Integer.parseInt(idStr);
                Vehicle v = vehicleDAO.searchVehicle(id);
                if (v != null) {
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[]{
                            v.getVehicleId(),
                            v.getVehicleName(),
                            v.getVehicleType(),
                            v.getRentPerDay(),
                            v.getAvailable()
                    });

                    // Set field values
                    txtVehicleName.setText(v.getVehicleName());
                    comboVehicleType.setSelectedItem(v.getVehicleType());
                    txtRentPerDay.setText(String.valueOf(v.getRentPerDay()));
                    comboAvailable.setSelectedItem(v.getAvailable());
                } else {
                    JOptionPane.showMessageDialog(this, "Vehicle not found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Search by Name
                List<Vehicle> list = vehicleDAO.searchVehiclesByName(name);
                if (!list.isEmpty()) {
                    tableModel.setRowCount(0);
                    for (Vehicle v : list) {
                        tableModel.addRow(new Object[]{
                                v.getVehicleId(),
                                v.getVehicleName(),
                                v.getVehicleType(),
                                v.getRentPerDay(),
                                v.getAvailable()
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No vehicles found matching: " + name, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vehicle ID must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
