import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;

    public LoginFrame() {
        setTitle("Vehicle Rental System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Colors & Theme (Premium Slate Dark Theme)
        Color bgDark = new Color(15, 23, 42);       // Tailwind slate-900 (Deep dark)
        Color bgCard = new Color(30, 41, 59);       // Tailwind slate-800 (Card background)
        Color textLight = new Color(248, 250, 252); // Tailwind slate-50 (Near white)
        Color borderSlate = new Color(51, 65, 85);  // Tailwind slate-700
        Color accentBlue = new Color(14, 165, 233); // Tailwind sky-500 (Vibrant blue)
        Color accentRed = new Color(239, 68, 68);   // Tailwind rose-500 (Vibrant red)

        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgDark);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(bgDark);
        JLabel lblTitle = new JLabel("VEHICLE RENTAL SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(accentBlue); // Electric accent color
        headerPanel.add(lblTitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Fields Panel (Centered Card)
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(bgCard);
        fieldsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(textLight);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        fieldsPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setForeground(textLight);
        txtUsername.setBackground(bgDark);
        txtUsername.setCaretColor(textLight); // Critical for cursor visibility
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        fieldsPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(textLight);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        fieldsPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setForeground(textLight);
        txtPassword.setBackground(bgDark);
        txtPassword.setCaretColor(textLight); // Critical for cursor visibility
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderSlate, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        fieldsPanel.add(txtPassword, gbc);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        buttonsPanel.setBackground(bgDark);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setBackground(accentBlue);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(95, 34));
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setBackground(accentRed);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new Dimension(95, 34));
        btnCancel.setBorder(BorderFactory.createEmptyBorder());
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add action listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        getRootPane().setDefaultButton(btnLogin);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    new Dashboard().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
