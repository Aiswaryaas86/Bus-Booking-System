package busbooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;  // Import for JDateChooser
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class seat extends JFrame {

    private final JComboBox<String> busNoComboBox;
    private final JDateChooser dateChooser;
    private final JButton addButton;
    private final JButton cancelButton;
    private final JLabel busNoLabel;
    private final JLabel dateLabel;
    private Connection con; // Declare the Connection variable
    private PreparedStatement pst; // Declare the PreparedStatement variable

    public seat() {
        // Set up the frame properties
        connect(); 
        setTitle("Add Seat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10)); // Set layout for the frame

        // Initialize components
        busNoComboBox = new JComboBox<>(new String[]{"91000", "92000"});
        dateChooser = new JDateChooser();
        addButton = new JButton("ADD");
        cancelButton = new JButton("CANCEL");
        busNoLabel = new JLabel("Bus No");
        dateLabel = new JLabel("Date");
        
        Font boldFont = new Font("Segoe UI", Font.BOLD, 18);

        // Apply the bold font with increased size to all components
        busNoLabel.setFont(boldFont);
        dateLabel.setFont(boldFont);
        busNoComboBox.setFont(boldFont);
        dateChooser.setFont(boldFont);
        addButton.setFont(boldFont);
        cancelButton.setFont(boldFont);

        // Set background colors
        addButton.setBackground(new Color(219, 112, 147)); // Dark pink shade
        cancelButton.setBackground(new Color(219, 112, 147)); // Dark pink shade
        busNoLabel.setOpaque(true); // Set opaque to apply background color
        busNoLabel.setBackground(Color.GRAY);
        dateLabel.setOpaque(true); // Set opaque to apply background color
        dateLabel.setBackground(Color.GRAY);

        // Add components to the frame
        add(busNoLabel);
        add(busNoComboBox);
        add(dateLabel);
        add(dateChooser);
        add(addButton);
        add(cancelButton);

        // Add action listeners
        addButton.addActionListener((ActionEvent evt) -> {
            String busno = busNoComboBox.getSelectedItem().toString();
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
            String date = date_format.format(dateChooser.getDate());
            
            for (int i = 1; i <= 28; i++) {
                try {
                    int seats = i;
                    String status = "unbooked";
                    pst = con.prepareStatement("INSERT INTO seat(busno, seats, date, status) VALUES (?, ?, ?, ?)");
                    pst.setString(1, busno);
                    pst.setInt(2, seats);
                    pst.setString(3, date);
                    pst.setString(4, status);
                    pst.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(seat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            JOptionPane.showMessageDialog(this, "Seat Added!");
            dispose(); // Close the current window after adding the seats
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            dispose(); // Close the current window when cancel is clicked
        });
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver class name for MySQL 8.x
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/superspeed", "root", "");
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(seat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(seat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new seat().setVisible(true);
    }
}
