import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Vehicle Rental System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Theme Colors
        Color bgDark = new Color(15, 23, 42);       // Tailwind slate-900 (Deep dark)
        Color bgCard = new Color(30, 41, 59);       // Tailwind slate-800
        Color textLight = new Color(248, 250, 252); // Tailwind slate-50 (White text)
        Color textMuted = new Color(148, 163, 184); // Tailwind slate-400 (Muted description)
        Color accentBlue = new Color(14, 165, 233); // Sky-500
        Color accentRed = new Color(239, 68, 68);   // Rose-500

        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgDark);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(bgDark);
        headerPanel.setPreferredSize(new Dimension(780, 110));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(51, 65, 85))); // border bottom
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 0, 4, 0);

        JLabel lblTitle = new JLabel("VEHICLE RENTAL SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(accentBlue);
        headerPanel.add(lblTitle, gbc);

        gbc.gridy = 1;
        JLabel lblSubtitle = new JLabel("Admin Dashboard | System Management");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(textMuted);
        headerPanel.add(lblSubtitle, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Buttons Panel (Cards Grid)
        JPanel menuPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        menuPanel.setBackground(bgDark);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(45, 45, 45, 45));

        // Vehicle button
        JButton btnVehicle = createMenuCard("Vehicle Management", "Manage Cars and Bikes database", new Color(14, 165, 233), bgCard, textLight, textMuted);
        btnVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VehicleForm().setVisible(true);
            }
        });

        // Customer button
        JButton btnCustomer = createMenuCard("Customer Management", "Renter registrations and listings", new Color(16, 185, 129), bgCard, textLight, textMuted);
        btnCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CustomerForm().setVisible(true);
            }
        });

        // Rental button
        JButton btnRental = createMenuCard("Rental Management", "Rent vehicles and process returns", new Color(139, 92, 246), bgCard, textLight, textMuted);
        btnRental.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RentalForm().setVisible(true);
            }
        });

        menuPanel.add(btnVehicle);
        menuPanel.add(btnCustomer);
        menuPanel.add(btnRental);

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Footer/Exit Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 12));
        footerPanel.setBackground(new Color(30, 41, 59));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(51, 65, 85)));

        JButton btnExit = new JButton("Logout & Exit");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExit.setBackground(accentRed);
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setPreferredSize(new Dimension(130, 32));
        btnExit.setBorder(BorderFactory.createEmptyBorder());
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(Dashboard.this, 
                        "Are you sure you want to log out and exit?", "Confirm Exit", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        footerPanel.add(btnExit);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuCard(String title, String subtitle, Color accentColor, Color bgCard, Color textLight, Color textMuted) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw Card background
                g2.setColor(bgCard);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Draw accent color border stripe at the top of card
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), 8, 16, 16);
                g2.fillRect(0, 4, getWidth(), 4); // flatten bottom of the stripe

                // Draw title
                g2.setColor(textLight);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fmTitle = g2.getFontMetrics();
                int xTitle = (getWidth() - fmTitle.stringWidth(title)) / 2;
                int yTitle = 65;
                g2.drawString(title, xTitle, yTitle);

                // Draw subtitle
                g2.setColor(textMuted);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                FontMetrics fmSub = g2.getFontMetrics();
                
                // If the description is long, we can wrap it or draw it on two lines.
                // We split by spaces if too wide, or just print it nicely centered.
                int xSub = (getWidth() - fmSub.stringWidth(subtitle)) / 2;
                int ySub = 95;
                g2.drawString(subtitle, xSub, ySub);

                g2.dispose();
            }
        };

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(190, 150));
        
        return button;
    }
}
