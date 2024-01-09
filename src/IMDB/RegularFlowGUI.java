package IMDB;

import Users.Admin;
import Users.Regular;
import Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegularFlowGUI {

    private JFrame frame;
    private Regular regular; // Assume this is your admin class
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private  JFrame loginFrame;
    public RegularFlowGUI(Regular regular, JFrame loginFrame) {
        this.regular = regular;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        this.loginFrame = loginFrame;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Regular Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.DARK_GRAY); // IMDb color
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        frame.add(cardPanel, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setVisible(true);
        // Dropdown Menu
        JMenu actionsMenu = new JMenu("Menu");
        ImageIcon actionsIcon = new ImageIcon(getClass().getResource("resources/menu.jpg"));
        ImageIcon actionsIconResized = new ImageIcon(actionsIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        actionsMenu.setIcon(actionsIconResized);
        actionsMenu.setForeground(Color.WHITE);
        menuBar.add(actionsMenu);
        ViewClass viewClass = new ViewClass(this.regular, frame, cardPanel, cardLayout, loginFrame);

        // Add menu items
        addMenuItem(actionsMenu, "View actors", e -> viewClass.viewActors());
        addMenuItem(actionsMenu, "View movies", e -> viewClass.viewMovies());
        addMenuItem(actionsMenu, "View series", e -> viewClass.viewSeries());
        addMenuItem(actionsMenu, "View favourites", e -> viewClass.viewFavourites());
        addMenuItem(actionsMenu, "View notifications", e -> viewClass.viewNotifications());
        addMenuItem(actionsMenu, "Create request", e -> viewClass.createRequest());
        addMenuItem(actionsMenu,"Log out", e -> viewClass.logOut());
        menuBar.setSize(800, 100);
        frame.setJMenuBar(menuBar);

        // Card panel for displaying grids

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void addMenuItem(JMenu menu, String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);

        menuItem.setBackground(new Color(30, 30, 30)); // Slightly lighter gray for items
        menuItem.setForeground(Color.WHITE); // White text
        menuItem.setFont(new Font("Arial", Font.BOLD, 12)); // Custom font
        menuItem.addActionListener(actionListener);

        // Hover effect
        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(new Color(60, 60, 60)); // Darker shade on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(new Color(30, 30, 30)); // Original color when not hovered
            }
        });

        menu.add(menuItem);
    }


    private void logOut() {
    }

    private void createRequest() {
    }

    private void viewRequests() {
    }

    private void solveRequests() {
    }

    private void updateActor() {
    }

    private void updateProduction() {
    }

    private void removeUser() {
    }

    private void addUser() {
    }

    private void removeActor() {
    }

    private void removeProduction() {
    }

    private void addActionButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        frame.add(button);
    }

    private void addProduction() {
        // Implement your logic for adding a production
        // This might involve opening another window or dialog
    }

    private void addActor() {
        // Implement your logic for adding an actor
    }
}