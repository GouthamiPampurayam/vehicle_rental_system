public class Main {
    public static void main(String[] args) {
        // Automatically verify/create database and database tables
        DBConnection.initializeDatabase();

        // Launch the application GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
