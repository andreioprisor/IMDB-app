package IMDB;

import ProductionAndActors.*;
import ProductionAndActors.Series.Episode;
import ProductionAndActors.Series.Series;
import Requests.Request;
import Requests.RequestType;
import Users.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;

public class ViewClass {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private User user;
    private JFrame loginFrame;
    private Color color = new Color(36, 36, 36);
    boolean showTop = true;
    public ViewClass(User user, JFrame frame, JPanel cardPanel, CardLayout cardLayout, JFrame loginFrame) {
        this.frame = frame;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        this.user = user;
        this.loginFrame = loginFrame;
        createTopPanel();
        viewWelcome();
    }

    private void createTopPanel() {
        // Main layout of the frame
        frame.setLayout(new BorderLayout());

        // Top Panel with welcome message and search bar
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        topPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 160));
        topPanel.setBackground(color);
        topPanel.setForeground(Color.WHITE);


        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 3, 30, 10); // Provides padding between components
        JLabel welcomeLabel = new JLabel("Welcome back, " + user.getInformation().getUserName(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBackground(color);
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, c);

        c.gridy = 1;
        JTextArea searchField = new JTextArea(2, 25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search OMDB...")) {
                    searchField.setText("");
                    searchField.setForeground(color);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(color);
                    searchField.setText("Search OMDB...");
                }
            }
        });


        JButton searchButton = new JButton();
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setContentAreaFilled(false);
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        try {
            Image img = ImageIO.read(getClass().getResource("resources/search.png"));
            searchButton.setIcon(new ImageIcon(img.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchField.getText());
            }

            private void performSearch(String text) {
                IMDB instance = IMDB.getInstance();
                for (Actor actor : instance.getActors()) {
                    if (actor.getName().equals(text)) {
                        JScrollPane actorDetailPanel = createActorDetailPanel(actor);
                        cardPanel.add(actorDetailPanel, actor.getName());
                        cardLayout.show(cardPanel, actor.getName());
                        frame.revalidate();
                        frame.repaint();
                        return;
                    }
                }
                for (Movies movie : instance.getMovies()) {
                    if (movie.getName().equals(text)) {
                        JScrollPane movieDetailPanel = createMovieDetailPanel(movie);
                        cardPanel.add(movieDetailPanel, movie.getName());
                        cardLayout.show(cardPanel, movie.getName());
                        frame.revalidate();
                        frame.repaint();
                        return;
                    }
                }
                for (Series series : instance.getSeries()) {
                    if (series.getName().equals(text)) {
                        JScrollPane seriesDetailPanel = createSeriesDetailPanel(series);
                        cardPanel.add(seriesDetailPanel, series.getName());
                        cardLayout.show(cardPanel, series.getName());
                        frame.revalidate();
                        frame.repaint();

                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "No results found for " + text);
            }
        });
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setBackground(color);
        topPanel.add(searchPanel, c);

        frame.add(topPanel, BorderLayout.NORTH);

        // Content Panel for different views

        // Add your different view panels to contentPanel
        // For example: viewActors(), viewMovies(), etc.

        frame.add(cardPanel, BorderLayout.CENTER); // Add contentPanel to center
    }

    public void viewActors() {
        Dimension frameSize = frame.getSize(); // Get the current frame size

        JPanel actorListPanel = createActorListPanel();
        JPanel slbz = new JPanel();
        slbz.add(new JLabel("Actors"));
        slbz.setBackground(color);
        slbz.setForeground(Color.WHITE);

        // Create the main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(color);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // Fill space
        constraints.gridx = 0; // First column
        constraints.gridy = 0; // First row
        constraints.weightx = 0.66; // Takes 66% of horizontal space
        constraints.weighty = 1.0; // Takes 100% of vertical space

        // Set preferred size of actorListPanel based on frame size
        Dimension d = actorListPanel.getPreferredSize();
        d.width = (int) (frameSize.width * 0.66);
        actorListPanel.setPreferredSize(d);

        // Add actorListPanel with constraints
        mainPanel.add(actorListPanel, constraints);

        // Update constraints for the second panel
        constraints.gridx = 1; // Second column
        constraints.weightx = 0.34; // Takes remaining 34% of horizontal space

        // Set preferred size of slbz based on frame size

        // Add slbz with updated constraints
        mainPanel.add(slbz, constraints);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        actorListPanel.revalidate();
        actorListPanel.repaint();
        // Add main panel to card layout
        cardPanel.add(scrollPane, "ActorList");
        cardLayout.show(cardPanel, "ActorList");
        frame.revalidate();
        frame.repaint();
    }

    public void viewMovies() {
        JPanel moviesListPanel =new JPanel();
        IMDB instance = IMDB.getInstance();
        addMoviesListPanel(moviesListPanel, instance.getMovies());
        JPanel slbz = new JPanel();
        slbz.setLayout(new BoxLayout(slbz, BoxLayout.Y_AXIS));
        slbz.add(new JLabel("Movies"));
        slbz.setBackground(color);
        slbz.setForeground(Color.WHITE);

        // Create the main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(color);
        JButton filterButton = new JButton("Filter by genre");
        filterButton.setBackground(Color.YELLOW);
        filterButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Movies> filteredMovies = filterMovies();
                for (Movies s : filteredMovies) {
                    System.out.println(s.getName());
                }
                updateMoviesListPanel(filteredMovies, moviesListPanel);
            }
        });
        slbz.add(filterButton);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // Fill space
        constraints.gridx = 0; // First column
        constraints.gridy = 0; // First row
        constraints.weightx = 0.66; // Takes 66% of horizontal space
        constraints.weighty = 1.0; // Takes 100% of vertical space

        // Set preferred size of actorListPanel based on frame size
        Dimension d = moviesListPanel.getPreferredSize();
        d.width = (int) (frame.getWidth() * 0.66);
        moviesListPanel.setPreferredSize(d);

        // Add actorListPanel with constraints
        mainPanel.add(moviesListPanel, constraints);

        // Update constraints for the second panel
        constraints.gridx = 1; // Second column
        constraints.weightx = 0.34; // Takes remaining 34% of horizontal space

        // Set preferred size of slbz based on frame size

        // Add slbz with updated constraints
        mainPanel.add(slbz, constraints);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        moviesListPanel.revalidate();
        moviesListPanel.repaint();
        // Add main panel to card layout
        cardPanel.add(scrollPane, "ActorList");
        cardLayout.show(cardPanel, "ActorList");
        frame.revalidate();
        frame.repaint();
    }

    public void viewSeries(){
        List <Series> series = IMDB.getInstance().getSeries();

        JPanel seriesListPanel = new JPanel();
        addSeriesListPanel(seriesListPanel, series);
        JPanel slbz = new JPanel();
        slbz.add(new JLabel("Series"));
        slbz.setBackground(color);
        slbz.setForeground(Color.WHITE);
        JButton filterButton = new JButton("Filter");
        filterButton.setBackground(Color.YELLOW);
        filterButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Series> filteredSeries = filterSeries();
                for (Series s : filteredSeries) {
                    System.out.println(s.getName());
                }
                updateSeriesListPanel(filteredSeries, seriesListPanel);
            }
        });
        slbz.add(filterButton);
        // Create the main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(color);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // Fill space
        constraints.gridx = 0; // First column
        constraints.gridy = 0; // First row
        constraints.weightx = 0.66; // Takes 66% of horizontal space
        constraints.weighty = 1.0; // Takes 100% of vertical space

        // Set preferred size of actorListPanel based on frame size
        Dimension d = seriesListPanel.getPreferredSize();
        d.width = (int) (frame.getWidth() * 0.66);
        seriesListPanel.setPreferredSize(d);

        // Add actorListPanel with constraints
        mainPanel.add(seriesListPanel, constraints);

        // Update constraints for the second panel
        constraints.gridx = 1; // Second column
        constraints.weightx = 0.34; // Takes remaining 34% of horizontal space

        // Set preferred size of slbz based on frame size

        // Add slbz with updated constraints
        mainPanel.add(slbz, constraints);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        seriesListPanel.revalidate();
        seriesListPanel.repaint();
        // Add main panel to card layout
        cardPanel.add(scrollPane, "ActorList");
        cardLayout.show(cardPanel, "ActorList");
        frame.revalidate();
        frame.repaint();
    }
    private void updateMoviesListPanel(List<Movies> filteredMovies, JPanel moviesListPanel) {
        moviesListPanel.removeAll(); // Clear the existing movie cards
        addMoviesListPanel(moviesListPanel, filteredMovies);
        moviesListPanel.revalidate();
        moviesListPanel.repaint();
    }

    private List<Movies> filterMovies() {
        List<Movies> filteredSeries = new ArrayList<>();
        String genre = JOptionPane.showInputDialog(frame, "Enter genre: ");
        IMDB instance = IMDB.getInstance();
        for (Movies movie : instance.getMovies()) {
            System.out.println(movie.getGenres().toString());
            if (movie.getGenres().contains(Genre.valueOf(genre.toUpperCase()))) {
                System.out.println("Found genre");
                filteredSeries.add(movie);
            }
        }
        return filteredSeries;
    }
    private void updateSeriesListPanel(List<Series> filteredSeries, JPanel seriesListPanel) {
        seriesListPanel.removeAll(); // Clear the existing series cards
        addSeriesListPanel(seriesListPanel, filteredSeries);
        seriesListPanel.revalidate();
        seriesListPanel.repaint();
    }

    private List<Series> filterSeries() {
        List<Series> filteredSeries = new ArrayList<>();
        String genre = JOptionPane.showInputDialog(frame, "Enter genre: ");
        IMDB instance = IMDB.getInstance();
        for (Series series : instance.getSeries()) {
            System.out.println(series.getGenres().toString());
            if (series.getGenres().contains(Genre.valueOf(genre.toUpperCase()))) {
                System.out.println("Found genre");
                filteredSeries.add(series);
            }
        }
        return filteredSeries;
    }
    private JPanel createActorListPanel() {
        JPanel actorListPanel = new JPanel();
        actorListPanel.setLayout(new BoxLayout(actorListPanel, BoxLayout.Y_AXIS));
        actorListPanel.setBackground(Color.BLACK);

        IMDB instance = IMDB.getInstance();
        Integer i = 0;
        for (Actor actor : instance.getActors()) {
            i++;
            JPanel actorCard = createActorCard(actor, i);
            JPanel actorPanel = createPanelWithImage("resources/actor.png", actorCard, new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane actorDetailPanel = createActorDetailPanel(actor);
                    cardPanel.add(actorDetailPanel, actor.getName());
                    cardLayout.show(cardPanel, actor.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    actorCard.setBackground(Color.darkGray); // Darker shade on hover
                    actorCard.getParent().setBackground(Color.darkGray);
                    actorCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    actorCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    actorCard.getParent().setBackground(new Color(36, 36,36));
                    actorCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            });
            actorListPanel.add(actorPanel);
        }

        return actorListPanel;
    }

    private JPanel createActorCard(Actor actor, Integer i) {
        JPanel actorCard = new JPanel();
        actorCard.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        actorCard.setBackground(new Color(255, 255, 255));
        actorCard.setBackground(new Color(36, 36, 36));
        actorCard.setForeground(Color.white);

        JLabel nameLabel = new JLabel(i + "." + actor.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBackground(new Color(36, 36, 36));
        nameLabel.setForeground(Color.white);
        i = 0;
        String prods = "";

        for (String production : actor.getProductions().keySet()) {
            i++;
            prods += production + ", ";
            if (i == 2) {
                break;
            }
        }
        prods = prods + "...";
        JLabel duration = new JLabel("Productions: " + prods.toString());
        duration.setFont(new Font("Arial", Font.PLAIN, 14));
        duration.setBackground(new Color(36, 36, 36));
        duration.setForeground(Color.white);


        JLabel rating = new JLabel("Age: " + actor.getAge());
        rating.setFont(new Font("Arial", Font.PLAIN, 14));
        rating.setBackground(new Color(36, 36, 36));
        rating.setForeground(Color.white);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 3, 0, 10); // Provides padding between components

        actorCard.add(nameLabel, c);

        c.gridy = 1;
        actorCard.add(duration, c);
        c.gridy = 2;
        actorCard.add(rating, c);
        c.insets = new Insets(0, 300, 0, 10); // Provides padding between components
        // Add more actor info if needed

        if(user instanceof Staff) {
            if (((Staff)user).getProductionsAndActors().contains(actor)==false&&user instanceof Contributor ) {
                JButton favouritesButton = createFavouriteButton(actor, "resources/emptyHeart.png", "resources/fullHeart.png");
                actorCard.add(favouritesButton);
                JButton updateButton = createUpdateButton(actor, actorCard);
                return actorCard;
            }
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            JButton favouritesButton = createFavouriteButton(actor, "resources/emptyStar.png", "resources/fullStar.png");
            JButton removeButton = createRemoveButton(actor, "resources/openBin.png", "resources/closedBin.png",  actorCard);
            buttonPanel.setBackground(new Color(36, 36, 36));
            buttonPanel.add(favouritesButton);
            buttonPanel.add(removeButton);
            JButton updateButton = createUpdateButton(actor, actorCard);
            buttonPanel.add(updateButton);
            actorCard.add(buttonPanel, c);
        }
        else{
            JButton favouritesButton = createFavouriteButton(actor, "resources/emptyStar.png", "resources/fullStar.png");
            actorCard.add(favouritesButton);
        }
        return actorCard;
    }

    private Rating showRatingDialog() {
        JTextField ratingField = new JTextField(5);
        JTextField userNameField = new JTextField(10);
        JTextArea commentsArea = new JTextArea(5, 20);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Rating (1-10):"));
        panel.add(ratingField);
        panel.add(Box.createVerticalStrut(15)); // Spacer
        panel.add(new JLabel("Comments:"));
        panel.add(new JScrollPane(commentsArea));

        int result = JOptionPane.showConfirmDialog(frame, panel,
                "Enter Rating Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int rating = Integer.parseInt(ratingField.getText());
                String userName = userNameField.getText();
                String comments = commentsArea.getText();
                Rating ratingObject = new Rating();
                ratingObject.setRating(rating);
                ratingObject.setUserName(user.getInformation().getUserName());
                ratingObject.setComments(comments);
                return ratingObject;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid rating number.");
                return null;
            }
        } else {
            return null;
        }
    }

    private JButton createRatingButton(Production p, Component component) {
        JButton updateButton = new JButton("ADD RATING");
        updateButton.setBackground(Color.GREEN);
        updateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(173, 210, 161)); // Darker shade on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(Color.GREEN); // Original color when not hovered
            }
        });

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rating rating = showRatingDialog();
                if (rating != null) {
                    p.getRatings().add(rating);
                }
            }
        };
        updateButton.addActionListener(actionListener);
        return updateButton;
    }
    private JButton createRemoveButton(T t, String openBin, String closedBin, Component component) {
        JButton removeButton = new JButton("Delete");
        removeButton.setForeground(Color.white);
        removeButton.setContentAreaFilled(false);
        removeButton.setBorderPainted(false);
        removeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 0));
        // Set the initial icon based on whether the movie is a favorite
        try {
            Image img = ImageIO.read(getClass().getResource(closedBin));
            removeButton.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        // Action listener to add or remove from favorites

        removeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButtonIcon(removeButton, openBin);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButtonIcon(removeButton, closedBin);
            }
        });

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t instanceof Actor) {
                    // Remove from favorites
                    int response = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to remove this actor from the system?",
                            "Confirm Removal", // Dialog title
                            JOptionPane.YES_NO_OPTION, // Option type
                            JOptionPane.QUESTION_MESSAGE); // Message type
                    if (response == JOptionPane.YES_OPTION) {
                        ((Staff)user).removeActorSystem(((Actor) t).getName());
                        Component parent = component.getParent().getParent();
                        component.getParent().getParent().remove(component.getParent());
                        parent.revalidate();
                        parent.repaint();
                    }
                } else if (t instanceof Production) {
                    // Add to favorites
                    int response = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to remove this production from the system?",
                            "Confirm Removal", // Dialog title
                            JOptionPane.YES_NO_OPTION, // Option type
                            JOptionPane.QUESTION_MESSAGE); // Message type
                    if (response == JOptionPane.YES_OPTION) {
                        ((Staff)user).removeProductionSystem(((Production) t).getName());
                        Component parent = component.getParent().getParent();
                        component.getParent().getParent().remove(component.getParent());
                        parent.revalidate();
                        parent.repaint();
                    }
                }
            }
        };
        removeButton.addActionListener(actionListener);
        return removeButton;
    }

    private JButton createUpdateButton(T t, Component component) {
        JButton updateButton = new JButton("Edit");
        updateButton.setBackground(Color.GREEN);
        updateButton.setBorder((BorderFactory.createEmptyBorder(10, 15, 0, 10)));
        updateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(255, 255, 150)); // Darker shade on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(Color.YELLOW); // Original color when not hovered
            }
        });
        if(t instanceof Actor){
            updateButton.setText("Edit actor");
        }
        else{
            updateButton.setText("Edit production");
        }
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t instanceof Actor) {
                    JScrollPane updatePanel = createAddActorPanel((Actor) t);
                    cardPanel.add(updatePanel, "UpdateActor");
                    cardLayout.show(cardPanel, "UpdateActor");
                    frame.revalidate();
                    frame.repaint();
                    // Remove from favorites
                } else if (t instanceof Production) {
                   JScrollPane updatePanel = createAddMoviePanel((Movies) t);
                    cardPanel.add(updatePanel, "UpdateProduction");
                    cardLayout.show(cardPanel, "UpdateProduction");
                    frame.revalidate();
                    frame.repaint();
                }
            }
        };
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(151, 196, 137, 255)); // Darker shade on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(Color.GREEN); // Original color when not hovered
            }
        });
        updateButton.addActionListener(actionListener);
        // Set the initial icon based on whether the movie is a favorite
        return updateButton;
    }
    private JPanel addPanel(String text, Font font){
        JTextArea actorName = new JTextArea(text);
        actorName.setFont(font);
        actorName.setLineWrap(true);
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

    private JScrollPane createActorDetailPanel(Actor actor) {
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

        JScrollPane scrollPane = new JScrollPane(actorDetailPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }



    private void addMoviesListPanel(JPanel moviesListPanel,List<Movies> movies) {
        moviesListPanel.setLayout(new BoxLayout(moviesListPanel, BoxLayout.Y_AXIS));
        moviesListPanel.setBackground(Color.darkGray);
        JLabel title = new JLabel("Movies");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBackground(Color.DARK_GRAY);
        title.setForeground(Color.WHITE);
        moviesListPanel.add(title);

        IMDB instance = IMDB.getInstance();
        int i = 0;
        for (Movies movie : movies) {
            i++;
            JPanel movieCard = createMovieCard(movie, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane movieDetailPanel = createMovieDetailPanel(movie);
                    cardPanel.add(movieDetailPanel, movie.getName());
                    cardLayout.show(cardPanel, movie.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    movieCard.setBackground(Color.darkGray); // Darker shade on hover
                    movieCard.getParent().setBackground(Color.darkGray);
                    movieCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    movieCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    movieCard.getParent().setBackground(new Color(36, 36,36));
                    movieCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel moviePanel = createPanelWithImage(movie.getName().replace(" ", "").strip(), movieCard, mouseAdapter);


            if (moviePanel != null) {
                moviesListPanel.add(moviePanel);
            }
        }
    }

    private JPanel createPanelWithImage(String imgPath, Component component, MouseAdapter mouseAdapter) {
        JPanel moviePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        try {
            JLabel label = new JLabel();
            try {
                imgPath = "/home/oda/ANUL2/IMDB-app/out/production/IMDB-app/IMDB/resources/" + imgPath + ".jpg";
                System.out.println(imgPath);
                BufferedImage image = ImageIO.read(new File(imgPath));
                label = new JLabel(new ImageIcon(image.getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
            }
            catch (Exception e){
                BufferedImage image = ImageIO.read(new File("/home/oda/ANUL2/IMDB-app/out/production/IMDB-app/IMDB/resources/movie.png"));
                label = new JLabel(new ImageIcon(image.getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
            }
            moviePanel.setBackground(new Color(255, 255, 255));
            moviePanel.add(label);
            moviePanel.add(component);
            moviePanel.setBackground(new Color(36, 36, 36));
            moviePanel.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
            moviePanel.addMouseListener(mouseAdapter);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return moviePanel;
    }


    private JPanel createMovieCard(Movies movie, Integer i) {
        JPanel actorCard = new JPanel();
        actorCard.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        actorCard.setBackground(new Color(255, 255, 255));
        actorCard.setBackground(new Color(36, 36, 36));
        actorCard.setForeground(Color.white);



        JLabel nameLabel = new JLabel(i + "." + movie.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBackground(new Color(36, 36, 36));
        nameLabel.setForeground(Color.white);

        JLabel duration = new JLabel("Duration: " + movie.getDuration());
        duration.setFont(new Font("Arial", Font.PLAIN, 14));
        duration.setBackground(new Color(36, 36, 36));
        duration.setForeground(Color.white);


        JLabel rating = new JLabel("Rating: " + movie.getAvgRating());
        rating.setFont(new Font("Arial", Font.PLAIN, 14));
        rating.setBackground(new Color(36, 36, 36));
        rating.setForeground(Color.white);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 3, 0, 200); // Provides padding between components

        actorCard.add(nameLabel, c);

        c.gridy = 1;
        actorCard.add(duration, c);
        c.gridy = 2;
        actorCard.add(rating, c);
        c.insets = new Insets(0, 300, 0, 10); // Provides padding between components

        // Add more actor info if needed

        if(user instanceof Staff) {
            if (((Staff)user).getProductionsAndActors().contains(movie)==false&&user instanceof Contributor ) {
                JButton favouritesButton = createFavouriteButton(movie, "resources/emptyHeart.png", "resources/fullHeart.png");
                actorCard.add(favouritesButton);
                return actorCard;
            }
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            JButton favouritesButton = createFavouriteButton(movie, "resources/emptyStar.png", "resources/fullStar.png");
            JButton removeButton = createRemoveButton(movie, "resources/openBin.png", "resources/closedBin.png",  actorCard);
            JButton updateButton = createUpdateButton(movie, actorCard);
            buttonPanel.setBackground(new Color(36, 36, 36));
            buttonPanel.setOpaque(false);

            JPanel topButtons = new JPanel();
            topButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 36, 10));
            topButtons.setBackground(new Color(36, 36, 36));
            topButtons.add(removeButton);
            topButtons.add(updateButton);

            buttonPanel.add(topButtons);
            buttonPanel.add(favouritesButton);

            actorCard.add(buttonPanel, c);
        }
        else{
            JButton favouritesButton = createFavouriteButton(movie, "resources/emptyStar.png", "resources/fullStar.png");
            actorCard.add(favouritesButton);
            if (user instanceof Regular) {
                JButton ratingButton = createRatingButton(movie, actorCard);
                actorCard.add(ratingButton);
            }
        }
        return actorCard;
    }

    // Method to create a favorite button with dynamic behavior
    private JButton createFavouriteButton(T t, String iconPathAdd, String iconPathRemove) {
        JButton favoritesButton = new JButton("Add to Favorites");
        favoritesButton.setContentAreaFilled(false);
        favoritesButton.setMargin(new Insets(0, 0, 0, 0));
        favoritesButton.setBackground(color);
        favoritesButton.setForeground(Color.WHITE);
        favoritesButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // Set the initial icon based on whether the movie is a favorite
        try {
            String initialIconPath = user.getFavorites().contains(t) ? iconPathRemove : iconPathAdd;
            Image img = ImageIO.read(getClass().getResource(initialIconPath));
            favoritesButton.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        // Action listener to add or remove from favorites
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.getFavorites().contains(t)) {
                    // Remove from favorites
                    user.removeFavourite(t);
                    updateButtonIcon(favoritesButton, iconPathAdd);
                } else {
                    // Add to favorites
                    user.addFavourite(t);
                    updateButtonIcon(favoritesButton, iconPathRemove);
                }
            }
        };
        favoritesButton.addActionListener(actionListener);
        return favoritesButton;
    }

    // Helper method to update the button icon
    private void updateButtonIcon(JButton button, String iconPath) {
        try {
            Image img = ImageIO.read(getClass().getResource(iconPath));
            button.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    private JScrollPane createMovieDetailPanel(Movies movie) {
        JPanel movieDetailPanel = new JPanel();
        movieDetailPanel.setLayout(new BoxLayout(movieDetailPanel, BoxLayout.Y_AXIS));
        movieDetailPanel.setBackground(color);
        movieDetailPanel.add(addPanel(movie.getName(), new Font("Arial", Font.BOLD, 26)));

        movieDetailPanel.add(addPanel("Subject: ", new Font("Arial", Font.BOLD, 18)));
        movieDetailPanel.add(addPanel(movie.getSubject(), new Font("Arial", Font.ITALIC, 16)));

        movieDetailPanel.add(addPanel("Duration: " + movie.getDuration(), new Font("Arial", Font.BOLD, 18)));

        movieDetailPanel.add(addPanel("Actors: ", new Font("Arial", Font.PLAIN, 18)));
        String a = "";
        for (String actor : movie.getActors()) {
            a += actor + ", ";
        }
        movieDetailPanel.add(addPanel(a, new Font("Arial", Font.PLAIN, 16)));

        String b = "\n\nRatings: \n";
        for (Rating rating : movie.getRatings()) {
            b += rating.getUserName() + "\n" + rating.getRating() + "\n" + rating.getComments() + "\n\n";
        }

        movieDetailPanel.add(addPanel(b, new Font("Arial", Font.PLAIN, 16)));
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

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MovieList"));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        movieDetailPanel.add(Box.createVerticalGlue());
        movieDetailPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(movieDetailPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }


    private void addSeriesListPanel(JPanel seriesListPanel, List<Series> filteredSeries) {
        seriesListPanel.setLayout(new BoxLayout(seriesListPanel, BoxLayout.Y_AXIS));
        seriesListPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        seriesListPanel.setBackground(color);
        JLabel title = new JLabel("Series");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBackground(color);
        title.setForeground(Color.WHITE);
        seriesListPanel.add(title, BorderLayout.WEST);

        int i = 0;
        for (Series series : filteredSeries) {
            i++;
            JPanel seriesCard = createSeriesCard(series, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane seriesDetailPanel = createSeriesDetailPanel(series);
                    cardPanel.add(seriesDetailPanel, series.getName());
                    cardLayout.show(cardPanel, series.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    seriesCard.setBackground(Color.darkGray); // Darker shade on hover
                    seriesCard.getParent().setBackground(Color.darkGray);
                    seriesCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    seriesCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    seriesCard.getParent().setBackground(new Color(36, 36,36));
                    seriesCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel seriesPanel = null;
            try{
                seriesPanel = createPanelWithImage("resources/" + series.getName().toString() + ".jpg", seriesCard, mouseAdapter);
            } catch (Exception e) {
                seriesPanel = createPanelWithImage("resources/movies.png", seriesCard, mouseAdapter);
            }
            if (seriesPanel != null) {
                seriesListPanel.add(seriesPanel);
            }
        }
    }

    private JPanel createSeriesCard(Series series, Integer i) {
        JPanel actorCard = new JPanel();
        actorCard.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        actorCard.setBackground(new Color(36, 36, 36));
        actorCard.setForeground(Color.white);
        actorCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Set height


        JLabel nameLabel = new JLabel(i + "." + series.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBackground(new Color(36, 36, 36));
        nameLabel.setForeground(Color.white);

        JLabel duration = new JLabel("Seasons number: " + series.getSeasonsNr());
        duration.setFont(new Font("Arial", Font.PLAIN, 14));
        duration.setBackground(new Color(36, 36, 36));
        duration.setForeground(Color.white);


        JLabel rating = new JLabel("Rating: " + series.getAvgRating());
        rating.setFont(new Font("Arial", Font.PLAIN, 14));
        rating.setBackground(new Color(36, 36, 36));
        rating.setForeground(Color.white);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 3, 0, 200); // Provides padding between components

        actorCard.add(nameLabel, c);

        c.gridy = 1;
        actorCard.add(duration, c);
        c.gridy = 2;
        actorCard.add(rating, c);
        c.insets = new Insets(0, 300, 0, 10); // Provides padding between components
        // Add more actor info if needed

        if(user instanceof Staff) {
            if (((Staff)user).getProductionsAndActors().contains(series)==false&&user instanceof Contributor ) {
                JButton favouritesButton = createFavouriteButton(series, "resources/emptyStar.png", "resources/fullStar.png");
                actorCard.add(favouritesButton);
                return actorCard;
            }
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBackground(new Color(36, 36, 36));
            JButton favouritesButton = createFavouriteButton(series, "resources/emptyStar.png", "resources/fullStar.png");
            JButton removeButton = createRemoveButton(series, "resources/openBin.png", "resources/closedBin.png",  actorCard);
            buttonPanel.add(favouritesButton);
            buttonPanel.add(removeButton);
            actorCard.add(buttonPanel, c);
        }
        else{
            JButton favouritesButton = createFavouriteButton(series, "resources/emptyStar.png", "resources/fullStar.png");
            actorCard.add(favouritesButton);
            if (user instanceof Regular) {
                JButton ratingButton = createRatingButton(series, actorCard);
                actorCard.add(ratingButton);
            }
        }
        return actorCard;
    }

    private JScrollPane createSeriesDetailPanel(Series series) {
        JPanel seriesDetailPanel = new JPanel();
        seriesDetailPanel.setLayout(new BoxLayout(seriesDetailPanel, BoxLayout.Y_AXIS));
        seriesDetailPanel.setBackground(Color.BLACK);
        seriesDetailPanel.add(addPanel(series.getName(), new Font("Arial", Font.BOLD, 26)));

        seriesDetailPanel.add(addPanel("Subject: ", new Font("Arial", Font.BOLD, 18)));
        seriesDetailPanel.add(addPanel(series.getDescription(), new Font("Arial", Font.ITALIC, 16)));

        seriesDetailPanel.add(addPanel("Number of seasons: " + series.getSeasonsNr(), new Font("Arial", Font.BOLD, 18)));

        seriesDetailPanel.add(addPanel("Actors: ", new Font("Arial", Font.PLAIN, 18)));
        String a = "";
        for (String actor : series.getActors()) {
            a += actor + ", ";
        }
        seriesDetailPanel.add(addPanel(a, new Font("Arial", Font.PLAIN, 16)));

        seriesDetailPanel.add(addPanel("Ratings: ", new Font("Arial", Font.BOLD, 18)));
        String b = "";
        int i = 0;
        for (Rating rating : series.getRatings()) {
            i++;
            b += i + ". " + rating.getUserName() + "\n" + "Rating:" + rating.getRating() + "\n" + "Comment: " + rating.getComments() + "\n\n";
        }
        seriesDetailPanel.add(addPanel(b, new Font("Arial", Font.PLAIN, 16)));

        i = 0;

        for (String season : series.getSeries().keySet()) {
            i++;
            String c = "";
            seriesDetailPanel.add(addPanel("Season " + i + ": ", new Font("Arial", Font.BOLD, 18)));
            int j = 0;
            for (Episode episode : series.getSeries().get(season)) {
                j++;
                c += "    " + j + ". " + episode.getTitle() + " (" + episode.getDuration() + ")" + "\n";
            }
            seriesDetailPanel.add(addPanel(c, new Font("Arial", Font.PLAIN, 16)));
        }

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

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "SeriesList"));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        seriesDetailPanel.add(Box.createVerticalGlue());
        seriesDetailPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(seriesDetailPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    public void viewNotifications(){
        JScrollPane notificationsListPanel = createNotificationsListPanel();
        cardPanel.add(notificationsListPanel, "NotificationsList");
        cardLayout.show(cardPanel, "NotificationsList");

        frame.revalidate();
        frame.repaint();
    }

    private JScrollPane createNotificationsListPanel() {
        JPanel notificationsListPanel = new JPanel();
        notificationsListPanel.setLayout(new BoxLayout(notificationsListPanel, BoxLayout.Y_AXIS));
        notificationsListPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        notificationsListPanel.setBackground(Color.BLACK);
        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBackground(Color.BLACK);
        title.setForeground(Color.WHITE);
        notificationsListPanel.add(title);

        for (String notification : user.getNotifications()) {
            JPanel notificationCard = createNotificationCard(notification);
            notificationsListPanel.add(notificationCard);
        }
        JScrollPane scrollPane = new JScrollPane(notificationsListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    private JPanel createNotificationCard(String notification) {
        JPanel notificationCard = new JPanel();
        notificationCard.setLayout(new BoxLayout(notificationCard, BoxLayout.Y_AXIS));
        notificationCard.setMinimumSize(new Dimension(frame.getWidth(), 200));
        notificationCard.setMaximumSize(new Dimension(frame.getWidth(), 200));
        notificationCard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        notificationCard.setBackground(Color.BLACK);
        notificationCard.setForeground(Color.WHITE);
        JTextArea nameLabel = new JTextArea(notification);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setBackground(Color.BLACK);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setLineWrap(true);
        nameLabel.setWrapStyleWord(true);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notificationCard.add(nameLabel);
        // Add more notification info if needed
        notificationCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                notificationCard.setBackground(new Color(69, 69,69)); // Darker shade on hover
            }
            public void mouseExited(MouseEvent e) {
                notificationCard.setBackground(Color.BLACK); // Original color when not hovered
            }
        });

        return notificationCard;
    }

    public void viewFavourites() {
        JScrollPane favouritesListPanel = createFavouritesListPanel();
        cardPanel.add(favouritesListPanel, "FavouritesList");
        cardLayout.show(cardPanel, "FavouritesList");

        frame.revalidate();
        frame.repaint();
    }

    private JScrollPane createFavouritesListPanel() {
        JPanel favouritesListPanel = new JPanel();
        favouritesListPanel.setLayout(new BoxLayout(favouritesListPanel, BoxLayout.Y_AXIS));
        favouritesListPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        favouritesListPanel.setBackground(color);
        JLabel title = new JLabel("Favourites");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBackground(Color.BLACK);
        title.setForeground(Color.WHITE);
        favouritesListPanel.add(title);
        Integer i = 0;
        for (T t  : user.getFavourites()) {
            i++;
            JPanel favouriteCard = createFavouriteCard(t, i);
            favouritesListPanel.add(favouriteCard);
        }
        JScrollPane scrollPane = new JScrollPane(favouritesListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }


    private JPanel createFavouriteCard(T t, Integer i) {
        JPanel favouriteCard = new JPanel();

        if (t instanceof Actor){
            JPanel actorCard = createActorCard((Actor) t, i);
            JPanel actorPanel = createPanelWithImage("resources/movie.png", actorCard, new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane actorDetailPanel = createActorDetailPanel((Actor) t);
                    cardPanel.add(actorDetailPanel, t.getName());
                    cardLayout.show(cardPanel, t.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    actorCard.setBackground(Color.darkGray); // Darker shade on hover
                    actorCard.getParent().setBackground(Color.darkGray);
                    actorCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    actorCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    actorCard.getParent().setBackground(new Color(36, 36,36));
                    actorCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            });
            return actorPanel;
        } else if (t instanceof Movies) {
            JPanel movieCard = createMovieCard((Movies) t, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane movieDetailPanel = createMovieDetailPanel((Movies) t);
                    cardPanel.add(movieDetailPanel, t.getName());
                    cardLayout.show(cardPanel, t.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    movieCard.setBackground(Color.darkGray); // Darker shade on hover
                    movieCard.getParent().setBackground(Color.darkGray);
                    movieCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    movieCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    movieCard.getParent().setBackground(new Color(36, 36,36));
                    movieCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel moviePanel = null;
            try {
                moviePanel = createPanelWithImage("resources/" + t.getName().strip() + ".jpg", movieCard, mouseAdapter);
            }catch (Exception e){
                moviePanel = createPanelWithImage("resources/movies.png", movieCard, mouseAdapter);
            }
            return moviePanel;
        } else if (t instanceof Series) {
            JPanel seriesCard = createSeriesCard((Series) t, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane seriesDetailPanel = createSeriesDetailPanel((Series) t);
                    cardPanel.add(seriesDetailPanel, t.getName());
                    cardLayout.show(cardPanel, t.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    seriesCard.setBackground(Color.darkGray); // Darker shade on hover
                    seriesCard.getParent().setBackground(Color.darkGray);
                    seriesCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    seriesCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    seriesCard.getParent().setBackground(new Color(36, 36,36));
                    seriesCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel seriesPanel = null;
            try {
                seriesPanel = createPanelWithImage("resources/" + t.getName().strip() + ".jpg", seriesCard, mouseAdapter);
            }catch (Exception e){
                seriesPanel = createPanelWithImage("resources/movies.png", seriesCard, mouseAdapter);
            }
            return seriesPanel;
        }
        return null;
    }

    public void displayUserRequests() {
        // Main panel that holds individual request panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);  // Set background color

        // JScrollPane for scrollable functionality
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border if desired

        // Iterate over requests and create individual panels
        for (Request request : ((Staff)user).getRequests()) {
            JPanel requestPanel = new JPanel();
            requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
            requestPanel.setBorder(BorderFactory.createTitledBorder("Request Details"));
            requestPanel.setBackground(new Color(230, 230, 250)); // Light purple background

            // Styling function for labels
            Consumer<JLabel> styleLabel = label -> {
                label.setForeground(Color.DARK_GRAY);
                label.setFont(new Font("SansSerif", Font.PLAIN, 14));
                label.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
            };

            // Create and add labels for each field of the Request
            JLabel lblType = new JLabel("Type: " + request.getType().toString());
            JLabel lblDateTime = new JLabel("Date: " + request.getDateTime().toString());
            JLabel lblUserName = new JLabel("User: " + request.getName());
            JLabel lblProductionTitle = new JLabel("Title: " + request.getProductionTitle());
            JLabel lblDescription = new JLabel("Description: " + request.getDescription());
            JLabel lblSolverUsername = new JLabel("Solver: " + request.getSolverUsername());

            // Apply styling to labels
            styleLabel.accept(lblType);
            styleLabel.accept(lblDateTime);
            styleLabel.accept(lblUserName);
            styleLabel.accept(lblProductionTitle);
            styleLabel.accept(lblDescription);
            styleLabel.accept(lblSolverUsername);

            // Create Solve button with styling
            JButton btnSolve = new JButton("Solve");
            btnSolve.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnSolve.setBackground(new Color(144, 238, 144)); // Light green background
            btnSolve.setForeground(Color.WHITE); // White text
            btnSolve.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
            btnSolve.addActionListener(e -> {
                try{
                    ((Staff)(user)).solveRequestGUI(request);
                    JOptionPane.showMessageDialog(frame, "Request solved successfully!");
                    requestPanel.getParent().remove(requestPanel);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            });

            // Add components to the request panel
            requestPanel.add(lblType);
            requestPanel.add(lblDateTime);
            requestPanel.add(lblUserName);
            requestPanel.add(lblProductionTitle);
            requestPanel.add(lblDescription);
            requestPanel.add(lblSolverUsername);
            requestPanel.add(btnSolve);

            // Add the request panel to the main panel
            mainPanel.add(requestPanel);
        }

        // Add the main scrollable panel to the card layout
        cardPanel.add(scrollPane, "RequestList");
        cardLayout.show(cardPanel, "RequestList");
        frame.revalidate();
        frame.repaint();
    }




    public void addMovie() {
        JScrollPane addMoviePanel = createAddMoviePanel(null);
        cardPanel.add(addMoviePanel, "AddMovie");
        cardLayout.show(cardPanel, "AddMovie");

        frame.revalidate();
        frame.repaint();
    }

    private JScrollPane createAddMoviePanel(Movies movie) {
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new GridBagLayout());
        moviePanel.setBackground(color); // Light blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15); // Padding

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        Dimension fieldDimension = new Dimension(250, 30);

        // Add input fields with labels for each attribute
        JTextField titleField = new JTextField();
        addLabelFieldPair(moviePanel, "Title:", titleField, labelFont, fieldFont, gbc);
        JTextField durationField = new JTextField();
        addLabelFieldPair(moviePanel, "Duration:", durationField, labelFont, fieldFont, gbc);
        JTextField yearField = new JTextField();
        addLabelFieldPair(moviePanel, "Year:", yearField, labelFont, fieldFont, gbc);
        JTextArea directorsField = new JTextArea(5, 20);
        addLabelFieldPair(moviePanel, "Directors (comma-separated):", directorsField, labelFont, fieldFont, gbc);
        JTextArea actorsField = new JTextArea(5, 20);
        addLabelFieldPair(moviePanel, "Actors (comma-separated):", actorsField, labelFont, fieldFont, gbc);
        JTextArea genresField = new JTextArea(5, 20);
        addLabelFieldPair(moviePanel, "Genres (comma-separated):", genresField, labelFont, fieldFont, gbc);
        JTextArea subjectField = new JTextArea(5, 20);
        addLabelFieldPair(moviePanel, "Subject:", subjectField, labelFont, fieldFont, gbc);
        JTextField avgRatingField = new JTextField();
        addLabelFieldPair(moviePanel, "Average Rating:", avgRatingField, labelFont, fieldFont, gbc);
        JTextArea descriptionField = new JTextArea(5, 20);
        addLabelFieldPair(moviePanel, "Description:", descriptionField, labelFont, fieldFont, gbc);

        if (movie != null) {
            titleField.setText(movie.getTitle());
            durationField.setText(movie.getDuration());
            yearField.setText(String.valueOf(movie.getYear()));
            String directors = "";
            for (String director : movie.getRegisors()) {
                directors += director + ",";
            }
            directorsField.setText(directors);
            String actors = "";
            for (String actor : movie.getActors()) {
                actors += actor + ",";
            }
            actorsField.setText(actors);
            String genres = "";
            for (Genre genre : movie.getGenres()) {
                genres += genre + ",";
            }
            genresField.setText(genres);
            subjectField.setText(movie.getSubject());
            avgRatingField.setText(String.valueOf(movie.getAvgRating()));
            descriptionField.setText(movie.getDescription());
        }

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(new Color(245, 201, 60)); // Cornflower blue
        gbc.insets = new Insets(10, 15, 20, 15); // Bottom padding for the button
        moviePanel.add(submitButton, gbc);

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movies movie = new Movies();
                movie.setTitle(titleField.getText());
                movie.setDuration(durationField.getText());
                movie.setYear(Integer.parseInt((yearField.getText())));
                movie.setRegisors(Arrays.asList(directorsField.getText().split(",")));
                movie.setActors(Arrays.asList(actorsField.getText().split(",")));
                String[] genres = genresField.getText().split(",");
                for (String genre : genres) {
                    movie.getGenres().add(Genre.valueOf(genre.toUpperCase()));
                }
                movie.setSubject(subjectField.getText());
                movie.setAvgRating(Double.parseDouble(avgRatingField.getText()));
                movie.setDescription(descriptionField.getText());
                if(((Staff)user).addProductionSystem(movie)==1){
                    JOptionPane.showMessageDialog(frame, "Movie added successfully!");
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Movie already exists!");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(moviePanel);
        scrollPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private JTextField createTextField(String text, Font font){
        JTextField textField = new JTextField(text);
        textField.setFont(font);
        textField.setBackground(Color.darkGray);
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        return textField;
    }

    public void addSeries() {
        JScrollPane addSeriesPanel = createSeriesEntryPanel();
        cardPanel.add(addSeriesPanel, "AddSeries");
        cardLayout.show(cardPanel, "AddSeries");

        frame.revalidate();
        frame.repaint();
    }

    private JScrollPane createSeriesEntryPanel() {

        return null;
    }

    public void addActor() {
        JScrollPane addActorPanel = createAddActorPanel(null);
        cardPanel.add(addActorPanel, "AddActor");
        cardLayout.show(cardPanel, "AddActor");

        frame.revalidate();
        frame.repaint();
    }


    private JScrollPane createAddActorPanel(Actor actor) {
        JPanel actorPanel = new JPanel();
        actorPanel.setLayout(new GridBagLayout());
        actorPanel.setBackground(color); // Light blue background
        actorPanel.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15); // Padding

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        // Increase field height
        Dimension fieldDimension = new Dimension(250, 30);

        // Add input fields with labels
        JTextField nameField = new JTextField();
        addLabelFieldPair(actorPanel, "Name:", nameField, labelFont, fieldFont, gbc);
        JTextField productionsField = new JTextField();
        addLabelFieldPair(actorPanel, "Productions (format: name:type):", productionsField, labelFont, fieldFont, gbc);
        JTextArea biographyField = new JTextArea(5, 20);
        addLabelFieldPair(actorPanel, "Biography:", biographyField, labelFont, fieldFont, gbc);
        JTextField ageField = new JTextField();
        addLabelFieldPair(actorPanel, "Age:", ageField, labelFont, fieldFont, gbc);
        // Submit Button

        if(actor!=null){
            nameField.setText(actor.getName());
            String productions = "";
            for (String production : actor.getProductions().keySet()) {
                productions += production + ":" + actor.getProductions().get(production) + ",";
            }
            productionsField.setText(productions);
            biographyField.setWrapStyleWord(true);
            biographyField.setLineWrap(true);
            biographyField.setText(actor.getBiography());
            ageField.setText(String.valueOf(actor.getAge()));
        }

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.insets = new Insets(10, 15, 20, 15); // Bottom padding for the button
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setBackground(new Color(245, 201, 60)); // Cornflower blue
        actorPanel.add(submitButton, gbc);

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Actor actor = new Actor();
                    actor.setName(nameField.getText());
                    actor.setAge(Integer.parseInt(ageField.getText()));
                    actor.setBiography(biographyField.getText());
                    actor.setProductions(parseProductions(productionsField.getText()));
                    if(((Staff)user).addActorSystem(actor)==1){
                        JOptionPane.showMessageDialog(frame, "Actor added successfully!");
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Actor already exists!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(actorPanel);
        scrollPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        return scrollPane;
    }


    private Map<String, ProductionType> parseProductions(String prodString) {
        Map<String, ProductionType> productions = new HashMap<>();
        String[] prods = prodString.split(",");
        for (String p : prods) {
            String[] prod = p.split(":");
            productions.put(prod[0], ProductionType.valueOf(prod[1].toUpperCase())); // Replace with actual logic
        }
        return productions;
    }

    public void addStaff() {
        JScrollPane addStaffPanel = createAddUserPanel();
        cardPanel.add(addStaffPanel, "AddStaff");
        cardLayout.show(cardPanel, "AddStaff");

        frame.revalidate();
        frame.repaint();
    }

    private JScrollPane createAddUserPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridBagLayout());
        userPanel.setBackground(color); // Light blue background
        userPanel.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Padding

        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);

        // Add input fields with labels
        addLabelFieldPair(userPanel, "Name:", new JTextField(20), labelFont, fieldFont, gbc);
        addLabelFieldPair(userPanel, "Country:", new JTextField(20), labelFont, fieldFont, gbc);
        addLabelFieldPair(userPanel, "Id:", new JTextField(20), labelFont, fieldFont, gbc);
        addLabelFieldPair(userPanel, "Age:", new JTextField(20), labelFont, fieldFont, gbc);

        // Gender ComboBox with custom styling
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBackground(color);
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setFont(labelFont);
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"F", "M", "N"});
        genderComboBox.setFont(fieldFont);
        userPanel.add(genderLabel, gbc);
        userPanel.add(genderComboBox, gbc);

        // Birth Date
        addLabelFieldPair(userPanel, "Birth Date (YYYY-MM-DD):", new JTextField(20), labelFont, fieldFont, gbc);
        addLabelFieldPair(userPanel, "Email:", new JPasswordField(20), labelFont, fieldFont, gbc);
        addLabelFieldPair(userPanel, "Password:", new JPasswordField(20), labelFont, fieldFont, gbc);

        JLabel buttonLabel = new JLabel("Select role:");
        buttonLabel.setBackground(color);
        buttonLabel.setForeground(Color.WHITE);
        buttonLabel.setFont(labelFont);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Regular", "Contributor", "Admin"});
        roleComboBox.setFont(fieldFont);
        userPanel.add(buttonLabel, gbc);
        userPanel.add(roleComboBox, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.insets = new Insets(10, 15, 20, 15); // Increased bottom padding for the button
        submitButton.setBackground(new Color(245, 201, 60)); // Cornflower blue
        userPanel.add(submitButton, gbc);

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserFactory userFactory = new UserFactory();
                User.Information.InformationBuilder infoBuilder = new User.Information.InformationBuilder();
                try {
                    infoBuilder.setName(((JTextField) userPanel.getComponent(1)).getText())
                            .setCountry(((JTextField) userPanel.getComponent(3)).getText())
                            .setName(((JTextField) userPanel.getComponent(5)).getText())
                            .setAge(Integer.parseInt(((JTextField) userPanel.getComponent(7)).getText()))
                            .setGender(((JComboBox<String>) userPanel.getComponent(9)).getSelectedItem().toString())
                            .setBirthDate(LocalDate.parse(((JTextField) userPanel.getComponent(11)).getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    String username = ((JTextField) userPanel.getComponent(15)).getText().strip();
                    infoBuilder.setUserName(username);
                    Credentials credentials = new Credentials(((JTextField) userPanel.getComponent(13)).getText(), ((JPasswordField) userPanel.getComponent(15)).getText());
                    infoBuilder.setCredentials(credentials);
                    AccountType accountType = AccountType.valueOf(((JComboBox<String>) userPanel.getComponent(17)).getSelectedItem().toString().toUpperCase());
                    User userAdded = userFactory.createUser(accountType, infoBuilder.build());
                    IMDB.getInstance().getUsers().add(userAdded);
                    ((Admin)user).getUsersAdded().add(userAdded);
                    JOptionPane.showMessageDialog(frame, "User added successfully!");
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(userPanel);
        scrollPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private void addLabelFieldPair(JPanel panel, String labelText, JComponent field, Font labelFont, Font fieldFont, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        label.setFont(labelFont);
        if( field instanceof JTextArea) {
            ((JTextArea) field).setWrapStyleWord(true);
            ((JTextArea) field).setLineWrap(true);
        }
        field.setFont(fieldFont);
        field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        field.setPreferredSize(new Dimension(200, 30));
        panel.add(label, gbc);
        panel.add(field, gbc);
    }

    public void removeStaff() {

    }

    public void createRequest() {
        JScrollPane createRequestPanel = createRequestPanel();
        cardPanel.add(createRequestPanel, "CreateRequest");
        cardLayout.show(cardPanel, "CreateRequest");

        frame.revalidate();
        frame.repaint();
    }

    private JScrollPane createRequestPanel() {
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new GridBagLayout());
        requestPanel.setBackground(color); // Light blue background
        requestPanel.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15); // Padding

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);

        Dimension fieldDimension = new Dimension(250, 30);

        // Title label
        JLabel titleLabel = new JLabel("Create a Request");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(10, 15, 20, 15); // Increased top padding for the title
        requestPanel.add(titleLabel, gbc);

        // Reset insets for input fields
        gbc.insets = new Insets(10, 15, 10, 15);

        // Add input fields with labels for each attribute
        JLabel genderLabel = new JLabel("Request type:");
        genderLabel.setBackground(color);
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setFont(labelFont);
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"ACTOR_ISSUE", "MOVIE_ISSUE", "DELETE_ACCOUNT", "OTHERS"});
        genderComboBox.setFont(fieldFont);
        requestPanel.add(genderLabel, gbc);
        requestPanel.add(genderComboBox, gbc);

        JTextField nameField = new JTextField();
        addLabelFieldPair(requestPanel, "User Name:", nameField, labelFont, fieldFont, gbc);
        JTextField productionTitleField = new JTextField();
        addLabelFieldPair(requestPanel, "Production Title (leave empty if not related):", productionTitleField, labelFont, fieldFont, gbc);
        JTextArea descriptionField = new JTextArea(5, 20);
        addLabelFieldPair(requestPanel, "Description:", descriptionField, labelFont, fieldFont, gbc);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.insets = new Insets(10, 15, 20, 15); // Bottom padding for the button
        submitButton.setBackground(new Color(245, 201, 60)); // Cornflower blue
        requestPanel.add(submitButton, gbc);

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IMDB instance = IMDB.getInstance();
                Request request = new Request(RequestType.valueOf(genderComboBox.getSelectedItem().toString().toUpperCase()),
                        nameField.getText(),
                        productionTitleField.getText(),
                        descriptionField.getText());
                try {
                    instance.getRequestsHolder().addRequest(request.getType(), request.getName(), request.getProductionTitle(), request.getDescription());
                    JOptionPane.showMessageDialog(frame, "Request added successfully! " + request.getName());
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(requestPanel);
        scrollPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        return scrollPane;
    }
    public void viewUserInformation() {
        JPanel userInformationPanel = createUserPanel(user.getInformation());
        JScrollPane userInformationListPanel = new JScrollPane(userInformationPanel);
        cardPanel.add(userInformationListPanel, "UserInformation");
        cardLayout.show(cardPanel, "UserInformation");

        frame.revalidate();
        frame.repaint();
    }

    public JPanel createUserPanel(User.Information info) {
        // Create the panel with BoxLayout for vertical alignment
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(this.color); // IMDB black
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("User Information");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBackground(this.color);
        title.setForeground(Color.WHITE);
        infoPanel.add(title);

        JLabel profilePicture = new JLabel();
        profilePicture.setIcon(new ImageIcon("resources/profilePicture.png"));
        profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(profilePicture);

        JLabel userExperience = new JLabel("Your Experience: " + this.user.getExperience());
        // Create labels with IMDB styling
        addStyledLabel(infoPanel, "ID: " + info.getId(), true);
        addStyledLabel(infoPanel, "Name: " + info.getName(), false);
        addStyledLabel(infoPanel, "Country: " + info.getCountry(), false);
        addStyledLabel(infoPanel, "Username: " + info.getUserName(), false);
        addStyledLabel(infoPanel, "Age: " + info.getAge(), false);
        addStyledLabel(infoPanel, "Gender: " + info.getGender(), false);
        addStyledLabel(infoPanel, "Birth Date: " + info.getBirthDate().toString(), false);
        addStyledLabel(infoPanel, "Email: " + info.getCredentials().getEmail(), false);

        return infoPanel;
    }

    private void addStyledLabel(JPanel panel, String text, boolean isTitle) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(245, 197, 24)); // IMDB gold for text
        label.setFont(new Font("Arial", isTitle ? Font.BOLD : Font.PLAIN, isTitle ? 20 : 16));
        panel.add(label);
    }


    private void viewWelcome() {
        JPanel welcomePanel = createWelcomePanel();
        cardPanel.add(welcomePanel, "Welcome");
        cardLayout.show(cardPanel, "Welcome");

        frame.revalidate();
        frame.repaint();
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomePanel.setBackground(this.color); // IMDB black
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel profilePicture = new JLabel();
        profilePicture.setIcon(new ImageIcon("resources/profilePicture.png"));
        profilePicture.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(profilePicture);

        JLabel userExperience = new JLabel("Your Experience: " + this.user.getExperience());
        userExperience.setFont(new Font("Arial", Font.BOLD, 16));
        userExperience.setBackground(this.color);
        userExperience.setForeground(Color.WHITE);
        userExperience.setBorder(BorderFactory.createEmptyBorder(22, 22, 10, 10));
        welcomePanel.add(userExperience);

        JPanel actorListPanel = new JPanel();
        actorListPanel.setLayout(new BoxLayout(actorListPanel, BoxLayout.Y_AXIS));
        actorListPanel.setBackground(color);

        IMDB instance = IMDB.getInstance();
        Integer i = 0;

        JLabel welcomeLabel = new JLabel("Reccomendeded for you:");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBackground(color);
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 22, 10));
        welcomePanel.add(welcomeLabel);
        for (Actor actor : instance.getActors()) {
            i++;
            JPanel actorCard = createActorCard(actor, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane actorDetailPanel = createActorDetailPanel(actor);
                    cardPanel.add(actorDetailPanel, actor.getName());
                    cardLayout.show(cardPanel, actor.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    actorCard.setBackground(Color.darkGray); // Darker shade on hover
                    actorCard.getParent().setBackground(Color.darkGray);
                    actorCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    actorCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    actorCard.getParent().setBackground(new Color(36, 36,36));
                    actorCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel actorPanel = createPanelWithImage("resources/" + actor.getName().replace(" ","")+ ".jpg", actorCard, mouseAdapter);
            if (i==3){
                i=2;
                break;
            }
            actorListPanel.add(actorPanel);
        }

        for (Movies movie : instance.getMovies()) {
            i++;
            JPanel movieCard = createMovieCard(movie, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane movieDetailPanel = createMovieDetailPanel(movie);
                    cardPanel.add(movieDetailPanel, movie.getName());
                    cardLayout.show(cardPanel, movie.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    movieCard.setBackground(Color.darkGray); // Darker shade on hover
                    movieCard.getParent().setBackground(Color.darkGray);
                    movieCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    movieCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    movieCard.getParent().setBackground(new Color(36, 36,36));
                    movieCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel moviePanel = createPanelWithImage("resources/movie.png", movieCard, mouseAdapter);
            try {
                moviePanel = createPanelWithImage("resources/" + movie.getName().replace(" ", "") + ".jpg", movieCard, mouseAdapter);
            }
            catch (Exception e){
                moviePanel = createPanelWithImage("resources/movie.png", movieCard, mouseAdapter);
            }
            if (i==5){
                i=4;
                break;
            }
            actorListPanel.add(moviePanel);
        }

        for (Series series : instance.getSeries()) {
            i++;
            JPanel seriesCard = createSeriesCard(series, i);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JScrollPane seriesDetailPanel = createSeriesDetailPanel(series);
                    cardPanel.add(seriesDetailPanel, series.getName());
                    cardLayout.show(cardPanel, series.getName());
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    seriesCard.setBackground(Color.darkGray); // Darker shade on hover
                    seriesCard.getParent().setBackground(Color.darkGray);
                    seriesCard.getComponent(3).setBackground(Color.darkGray);
                    seriesCard.getComponent(3).setBackground(Color.darkGray);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    seriesCard.setBackground(new Color(36, 36,36)); // Original color when not hovered
                    seriesCard.getParent().setBackground(new Color(36, 36,36));
                    seriesCard.getComponent(3).setBackground(new Color(36, 36,36));
                }
            };
            JPanel seriesPanel = null;
            try {
                seriesPanel = createPanelWithImage("resources/" + series.getName().replace(" ","")+ ".jpg", seriesCard, mouseAdapter);
            }
            catch (Exception e){
                seriesPanel = createPanelWithImage("resources/movie.png", seriesCard, mouseAdapter);
            }
            if (i==7){
                break;
            }
            actorListPanel.add(seriesPanel);
        }
        JScrollPane scrollPane = new JScrollPane(actorListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        welcomePanel.add(scrollPane);
        return welcomePanel;
    }


    public void logOut() {
        cardPanel.removeAll();
        frame.dispose();
        loginFrame.setVisible(true);
    }
}
