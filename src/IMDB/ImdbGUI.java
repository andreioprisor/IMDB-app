package IMDB;

import Requests.RequestType;
import Users.Admin;
import Users.Contributor;
import Users.Regular;
import Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImdbGUI {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public ImdbGUI() {
        initialize();
        IMDB instance = IMDB.getInstance();
        instance.loadActors();
        instance.loadProductions();
        instance.loadAccounts();
        instance.loadRequests();
    }

    private void initialize() {
        // Frame initialization
        frame = new JFrame("IMDB App");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY); // Set background color
        frame.setLayout(null); // Using null layout for absolute positioning

        // Email field
        emailField = new JTextField();
        emailField.setBounds(100, 50, 200, 20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font
        frame.add(emailField);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(100, 100, 200, 20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font
        frame.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        loginButton.setBackground(Color.LIGHT_GRAY); // Button color
        loginButton.setForeground(Color.BLACK); // Text color
        frame.add(loginButton);

        // Message label
        messageLabel = new JLabel("");
        messageLabel.setBounds(100, 200, 300, 20);
        messageLabel.setForeground(Color.WHITE); // Set text color
        frame.add(messageLabel);

        // Button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        frame.setVisible(true);
    }


    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        // Add your validation logic here
        User user = validateUser(email, password);
        if (user == null) {
            messageLabel.setText("Invalid credentials! Please try again.");
        } else {
            frame.setVisible(false);
            switch (user.getAccountType()) {
                case ADMIN:
                    System.out.println(((Admin) (user)).getNotifications().toString());
                    AdminFlowGUI adminFlowGUI = new AdminFlowGUI((Admin) (user), frame);
                    break;
                case CONTRIBUTOR:
                    ContributorFlowGUI contributorFlowGUI = new ContributorFlowGUI((Contributor) (user), frame);
                    break;
                case REGULAR:
                    RegularFlowGUI regularFlowGUI = new RegularFlowGUI((Regular) (user), frame);
                    break;
            }
        }
    }

    // Mock validation method (replace with your actual validation logic)
    public User validateUser(String email, String password){
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return null;
        }
        java.util.List<User> Users = instance.getUsers();
        for(User user: Users){
            String userEmail = user.getInformation().getCredentials().getEmail();
            String userPassword = user.getInformation().getCredentials().getPassword();
            if(userEmail.compareTo(email)==0 && userPassword.compareTo(password)==0){
                return user;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ImdbGUI app = new ImdbGUI();
                    IMDB instance = IMDB.getInstance();
                    instance.getRequestsHolder().addRequest(RequestType.DELETE_ACCOUNT, "user1", "production1", "description1");
                    System.out.println(instance.getUsers().size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
