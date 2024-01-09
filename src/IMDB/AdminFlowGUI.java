package IMDB;

import ProductionAndActors.Actor;
import Requests.Request;
import Users.Admin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class AdminFlowGUI {

    private JFrame frame;
    private Admin admin; // Assume this is your admin class
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JFrame login;

    public AdminFlowGUI(Admin admin, JFrame login) {
        this.admin = admin;
        this.login = login;
        initializee();
    }
    private void initializee() {
        frame = new JFrame("Admin Dashboard");
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
        JMenu actionsMenu = new JMenu("Menu");
        try {
           BufferedImage img = ImageIO.read(getClass().getResource("resources/menu.jpg"));
           ImageIcon icon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
           actionsMenu.setIcon(icon);
        }catch (Exception e) {
            System.out.println("Image not found");
        }
        actionsMenu.setBackground(new Color(36, 36, 36)); // Slightly lighter gray for items
        actionsMenu.setForeground(Color.white); // White text
        actionsMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                actionsMenu.setBackground(new Color(69, 69,69)); // Darker shade on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                actionsMenu.setBackground(new Color(36, 36, 36)); // Original color when not hovered
            }
        });


        menuBar.add(actionsMenu);
        menuBar.setBackground(new Color(36, 36, 36)); // Slightly lighter gray for items
        menuBar.setForeground(Color.white); // White text
        menuBar.setSize(Integer.MAX_VALUE, 60);

        ViewClass viewClass = new ViewClass(this.admin, frame, cardPanel, cardLayout, login);
        // Add menu items
        addMenuItem(actionsMenu, "Add Movie", e -> viewClass.addMovie());
        addMenuItem(actionsMenu, "Add Actor", e -> viewClass.addActor());
        addMenuItem(actionsMenu, "Remove Production", e -> removeProduction());
        addMenuItem(actionsMenu, "Add User", e -> viewClass.addStaff());
        addMenuItem(actionsMenu, "Remove User", e -> removeUser());
        addMenuItem(actionsMenu, "Solver requests", e -> viewClass.displayUserRequests());
        addMenuItem(actionsMenu, "View actors", e -> viewClass.viewActors());
        addMenuItem(actionsMenu, "View movies", e -> viewClass.viewMovies());
        addMenuItem(actionsMenu, "View series", e -> viewClass.viewSeries());
        addMenuItem(actionsMenu, "View favourites", e -> viewClass.viewFavourites());
        addMenuItem(actionsMenu, "View notifications", e -> viewClass.viewNotifications());
        addMenuItem(actionsMenu,"Log out", e -> viewClass.logOut());
        frame.setJMenuBar(menuBar);

        // Card panel for displaying grids

        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    private void addMenuItem(JMenu menu, String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setBackground(new Color(36, 36, 36)); // Slightly lighter gray for items
        menuItem.setForeground(Color.white); // White text
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    private void viewActors() {

        JScrollPane actorListPanel = createActorListPanel();
        cardPanel.add(actorListPanel, "ActorList");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    private JScrollPane createActorListPanel() {
        JPanel actorListPanel = new JPanel();
        actorListPanel.setLayout(new BoxLayout(actorListPanel, BoxLayout.Y_AXIS));
        actorListPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        actorListPanel.setBackground(Color.BLACK);
        JLabel title = new JLabel("Actors");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBackground(Color.BLACK);
        title.setForeground(Color.WHITE);
        actorListPanel.add(title);

        IMDB instance = IMDB.getInstance();
        for (Actor actor : instance.getActors()) {
            JPanel actorCard = createActorCard(actor);
            actorListPanel.add(actorCard);
        }
        JScrollPane scrollPane = new JScrollPane(actorListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    private JPanel createActorCard(Actor actor) {
        JPanel actorCard = new JPanel();
        actorCard.setLayout(new BoxLayout(actorCard, BoxLayout.Y_AXIS));
        actorCard.setMinimumSize(new Dimension(frame.getWidth(), 150));
        actorCard.setMaximumSize(new Dimension(frame.getWidth(), 150));
        actorCard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        actorCard.setBackground(Color.BLACK);
        actorCard.setForeground(Color.WHITE);
        JLabel nameLabel = new JLabel(actor.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setBackground(Color.BLACK);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel BiographyLabel = new JLabel("Biography: " + actor.getBiography());
        BiographyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        BiographyLabel.setBackground(Color.BLACK);
        BiographyLabel.setForeground(Color.WHITE);
        BiographyLabel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 10));
        actorCard.add(nameLabel);
        actorCard.add(BiographyLabel);
        // Add more actor info if needed

        actorCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel actorDetailPanel = createActorDetailPanel(actor);
                cardPanel.add(actorDetailPanel, actor.getName());
                cardLayout.show(cardPanel, actor.getName());
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                actorCard.setBackground(new Color(69, 69,69)); // Darker shade on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                actorCard.setBackground(Color.BLACK); // Original color when not hovered
            }
        });

        return actorCard;
    }

    private JPanel addPanel(String text, Font font){
        JTextArea actorName = new JTextArea(text);
        actorName.setFont(font);
        actorName.setEditable(false);
        actorName.setLineWrap(true);
        actorName.setWrapStyleWord(true);
        actorName.setBorder(BorderFactory.createCompoundBorder(
                actorName.getBorder(),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        actorName.setBackground(Color.BLACK);
        actorName.setForeground(Color.WHITE);
        JPanel actorNamePanel = new JPanel(new BorderLayout());
        actorNamePanel.setBackground(Color.BLACK);
        actorNamePanel.add(actorName);
        actorNamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Set height
        return actorNamePanel;
    }

    private JPanel createActorDetailPanel(Actor actor) {
        JPanel actorDetailPanel = new JPanel();
        actorDetailPanel.setLayout(new BoxLayout(actorDetailPanel, BoxLayout.Y_AXIS));
        actorDetailPanel.setBackground(Color.BLACK);
        actorDetailPanel.add(addPanel(actor.getName(), new Font("Arial", Font.BOLD, 26)));

        actorDetailPanel.add(addPanel("Biography: ", new Font("Arial", Font.BOLD, 18)));
        actorDetailPanel.add(addPanel(actor.getBiography(), new Font("Arial", Font.ITALIC, 16)));

        actorDetailPanel.add(addPanel("Productions: ", new Font("Arial", Font.BOLD, 18)));
        String a = "";
        for (String production : actor.getProductions().keySet()) {
            a += production + ", ";
        }
        actorDetailPanel.add(addPanel(a, new Font("Arial", Font.PLAIN, 16)));
        JButton backButton = new JButton("Go Back");
        backButton.setFont(new Font("Serif", Font.BOLD, 18)); // Set font style and size
        backButton.setForeground(Color.BLACK); // Set text color
        backButton.setBackground(Color.YELLOW); // Set background color

        // Add a hover effect
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(255, 255, 150)); // Darker shade on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.YELLOW); // Original color when not hovered
            }
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "ActorList"));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actorDetailPanel.add(Box.createVerticalGlue());
        actorDetailPanel.add(backButton);
        return actorDetailPanel;
    }
    private void viewProductions() {
    }

    private void viewNotifications() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Notifications");
        dialog.setSize(300, 400);
        dialog.setLayout(new BorderLayout());

        // Fetch notifications
        // Create a JList to display notifications
        JList<String> notificationList = new JList<>(admin.getNotifications().toArray(new String[0]));
        System.out.println(admin.getNotifications().toString());
        dialog.add(new JScrollPane(notificationList), BorderLayout.CENTER);

        dialog.setVisible(true);
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

        // Set font
        button.setFont(new Font("Arial", Font.BOLD, 14));

        // Set button color
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);

        // Set button size
        button.setPreferredSize(new Dimension(150, 40));

        // Add padding
        button.setMargin(new Insets(5, 15, 5, 15));

        // Set border (optional)
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        // Set hover effect (optional)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.YELLOW);
            }
        });
        // Add action listener
        button.addActionListener(actionListener);
        // Add to frame
        frame.add(button);
    }


    private void addProduction() {
        // Implement your logic for adding a production
        // This might involve opening another window or dialog
    }

    private void addActor() {
        // Implement your logic for adding an actor
    }

    private void addUsers(){
//        addToPanel(panel, new JLabel("Gender:"), labelFont, textColor, 0, 2, c);
//        JRadioButton maleButton = new JRadioButton("Male");
//        JRadioButton femaleButton = new JRadioButton("Female");
//        ButtonGroup genderGroup = new ButtonGroup();
//        genderGroup.add(maleButton);
//        genderGroup.add(femaleButton);
//        JPanel genderPanel = new JPanel(new FlowLayout());
//        genderPanel.add(maleButton);
//        genderPanel.add(femaleButton);
    }

    private void removeUsesr(){

    }

}
