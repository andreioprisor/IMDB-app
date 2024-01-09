package IMDB;

import Requests.Request;
import Users.Admin;
import Users.Contributor;
import Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContributorFlowGUI {

    private JFrame frame;
    private Contributor contributor; // Assume this is your admin class

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private  JFrame loginFrame;
    public ContributorFlowGUI(Contributor contributor, JFrame loginFrame) {
        this.contributor = contributor;
        this.loginFrame = loginFrame;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Contributor Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        frame.add(cardPanel, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setVisible(true);
        // Dropdown Menu
        JMenu actionsMenu = new JMenu("Actions");
        menuBar.add(actionsMenu);
        ViewClass viewClass = new ViewClass(this.contributor, frame, cardPanel, cardLayout, loginFrame);
        // Add menu items
        addMenuItem(actionsMenu, "Add Production", e -> addProduction());
        addMenuItem(actionsMenu, "Add Actor", e -> addActor());
        addMenuItem(actionsMenu, "Remove Production", e -> removeProduction());
        addMenuItem(actionsMenu, "Remove Actor", e -> removeActor());
        addMenuItem(actionsMenu, "Update production", e -> updateProduction());
        addMenuItem(actionsMenu, "Update actor", e -> updateActor());
        addMenuItem(actionsMenu, "Solver requests", e -> viewClass.displayUserRequests());
        addMenuItem(actionsMenu, "View actors", e -> viewClass.viewActors());
        addMenuItem(actionsMenu, "View movies", e -> viewClass.viewMovies());
        addMenuItem(actionsMenu, "View series", e -> viewClass.viewSeries());
        addMenuItem(actionsMenu, "View favourites", e -> viewClass.viewFavourites());
        addMenuItem(actionsMenu, "View notifications", e -> viewClass.viewNotifications());
        frame.setJMenuBar(menuBar);

        // Card panel for displaying grids

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void addMenuItem(JMenu menu, String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setBackground(Color.GRAY); // Slightly lighter gray for items
        menuItem.setForeground(Color.white); // White text
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    private void viewRequests() {

    }

    private void solveRequests() {
    }

    private void updateActor() {
    }

    private void updateProduction() {
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