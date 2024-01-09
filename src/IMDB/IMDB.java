package IMDB;

import ProductionAndActors.*;
import ProductionAndActors.Series.Episode;
import ProductionAndActors.Series.Series;
import Requests.Request;
import Requests.RequestType;
import Requests.RequestsHolder;
import Users.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class IMDB {
    private static IMDB instance;
    private List<Actor> actors;
    private List<Movies> movies;
    private List<Series> series;
    private List<User> Users;
    private RequestsHolder requestsHolder;
    private IMDB(){
        this.actors = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.series = new ArrayList<>();
        this.Users = new ArrayList<>();
        this.requestsHolder = new RequestsHolder();
    }
    public static IMDB getInstance(){
        if(instance==null){
            synchronized (IMDB.class){
                instance = new IMDB();
            }
        }
        return instance;
    }
    public RequestsHolder getRequestsHolder() {
        return requestsHolder;
    }

    public void setRequestsHolder(RequestsHolder requestsHolder) {
        this.requestsHolder = requestsHolder;
    }
    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }
    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    // Getters and Setters for movies
    public List<Movies> getMovies() {
        return movies;
    }

    public void setMovies(List<Movies> movies) {
        this.movies = movies;
    }

    // Getters and Setters for series
    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public User validateUser(String email, String password){
        for(User user: Users){
            String userEmail = user.getInformation().getCredentials().getEmail();
            String userPassword = user.getInformation().getCredentials().getPassword();
            if(userEmail.compareTo(email)==0 && userPassword.compareTo(password)==0){
                return user;
            }
        }
        return null;
    }

    public void updateActorMethod(User user) {
        System.out.println("Enter actor name: ");
        Scanner scanner = new Scanner(System.in);
        String actorName = scanner.nextLine();
        Actor actor1 = new Actor();
        for (Actor a : instance.getActors()){
            if ( a.getName().compareTo(actorName) == 0 ){
                actor1 = a;
            }
        }
        if (actor1 != null){
            System.out.println("Choose what to change about the actor: ");
            System.out.println("  1. Name");
            System.out.println("  2. Biography");
            System.out.println("  3. Productions");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice){
                    case 1:
                        System.out.println("Enter new name: ");
                        String newName = scanner.nextLine();
                        actor1.setName(newName);
                        break;
                    case 2:
                        System.out.println("Enter new biography: ");
                        String newBiography = scanner.nextLine();
                        actor1.setBiography(newBiography);
                        break;
                    case 3:
                        System.out.println("Enter number of productions: ");
                        int noProductions = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noProductions; i++) {
                            System.out.println("Enter production name: ");
                            String prod = scanner.nextLine();
                            System.out.println("Enter production type: ");
                            String userInput = scanner.nextLine().toUpperCase();
                            try {
                                actor1.getProductions().put(prod, ProductionType.valueOf(userInput));
                            }catch (Exception e){}
                        }
                        ((Staff) user).updateActor(actor1);
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            }
            catch (Exception e){
                System.out.println("Invalid option!");
            }
        }
        else {
            System.out.println("Actor not found!");
        }
    }

    public void updateProductionMethod(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter production name: ");
        String name = scanner.nextLine();
        Production production = null;
        for (Production prod : instance.getMovies()){
            if ( prod.getTitle().compareTo(name) == 0 ){
                production = prod;
            }
        }
        for (Production prod : instance.getSeries()){
            if ( prod.getTitle().compareTo(name) == 0 ){
                production = prod;
            }
        }
        if (production != null){
            System.out.println("Choose what to change about the production: ");
            System.out.println("  1. Title");
            System.out.println("  2. Year");
            System.out.println("  3. Actors");
            System.out.println("  4. Regisors");
            System.out.println("  5. Genres");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice){
                    case 1:
                        System.out.println("Enter new title: ");
                        String newTitle = scanner.nextLine();
                        production.setTitle(newTitle);
                        break;
                    case 2:
                        System.out.println("Enter number of actors: ");
                        int noActors = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noActors; i++) {
                            System.out.println("Enter actor: ");
                            production.getActors().add(scanner.nextLine());
                        }
                        break;
                    case 3:
                        System.out.println("Enter number of regisors: ");
                        int noRegisors = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noRegisors; i++) {
                            System.out.println("Enter regisor: ");
                            production.getRegisors().add(scanner.nextLine());
                        }
                        break;
                    case 4:
                        System.out.println("Enter number of genres: ");
                        int noGenres = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noGenres; i++) {
                            System.out.println("Enter genre: ");
                            Genre genre = Genre.valueOf(scanner.nextLine());
                            boolean correct = false;
                            while (!correct) {
                                for (int j = 0; j < Genre.values().length; j++) {
                                    if (genre == Genre.values()[j]) {
                                        production.getGenres().add(genre);
                                        correct = true;
                                        break;
                                    }
                                }
                                if (!correct) {
                                    System.out.println("Invalid genre! Try again: ");
                                    genre = Genre.valueOf(scanner.nextLine());
                                }
                            }
                        }
                        ((Staff) user).updateProduction(production);
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;

                }
            }
            catch (Exception e){
                System.out.println("Invalid option!");
            }

        }
    }

    public void requestSolver (User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Those are your requests: \n");
        int i = 1;
        for (Request request : ((Staff) user).getRequests()) {
            System.out.println(i + ". " + request.getName());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Date: " + request.getDateTime());
            System.out.println("ProductionTitle: " + request.getProductionTitle());
            System.out.println("");
            i++;
        }
        if(i==1){
            System.out.println("You don't have any request!\n");
            return;
        }
        System.out.println("Choose action: (Enter the request index)");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            Request r = ((Staff) user).getRequests().get(choice - 1);
            ((Staff) user).solveRequest(r);
        } catch (Exception e){}
    }

    public void addProductionIMDB(User user) {
        Scanner scanner = new Scanner(System.in);
        boolean continueEditingProduction = true;
        while (continueEditingProduction) {
            System.out.println("Choose production type: ");
            System.out.println("  1. Movie");
            System.out.println("  2. Series");
            System.out.println("  3. Go back");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        Movies movie = new Movies();

                        System.out.println("Enter movie duration: ");
                        String duration = scanner.nextLine();
                        movie.setDuration(duration);

                        System.out.println("Enter movie year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        movie.setYear(year);

                        System.out.println("Enter movie name: ");
                        String name = scanner.nextLine();
                        movie.setTitle(name);

                        System.out.println("Enter number of genres: ");
                        int noGenres = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noGenres; i++) {
                            System.out.println("Enter genre: ");
                            String userInput = scanner.nextLine().toUpperCase();
                            Genre genre = Genre.valueOf(userInput);
                            movie.getGenres().add(genre);
                        }

                        System.out.println("Enter subject:  ");
                        movie.setSubject(scanner.nextLine());

                        System.out.println("Enter number of regisors: ");
                        int noRegisors = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noRegisors; i++) {
                            System.out.println("Enter regisor: ");
                            String regisor = scanner.nextLine();
                            movie.getRegisors().add(regisor);
                        }

                        System.out.println("Enter number of actors: ");
                        int noActors = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noActors; i++) {
                            System.out.println("Enter actor: ");
                            String actor = scanner.nextLine();
                            movie.getActors().add(actor);
                        }

                        System.out.println("Is this information correct? (y/n):");
                        System.out.println("Name: " + movie.getTitle() + "\n"
                                + "Year: " + movie.getYear() + "\n"
                                + "Duration: " + movie.getDuration() + "\n"
                                + "Regisors: " + movie.getRegisors() + "\n"
                                + "Actors: " + movie.getActors() + "\n"
                                + "Genres: " + movie.getGenres() + "\n");
                        switch (scanner.nextLine().charAt(0)) {
                            case 'y':
                                instance.getMovies().add(movie);
                                ((Staff) user).addProductionSystem(movie);
                                System.out.println("Movie " + movie.getTitle() + "added successfully!");
                                continueEditingProduction = false;
                                break;
                            case 'n':
                                break;
                            default:
                                System.out.println("Invalid option!");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input! " + e.toString() + "\n");
                        scanner.nextLine();
                    }
                    break;
                case 2:
                    Series series = new Series();
                    try {
                        System.out.println("Enter serie year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        series.setYear(year);

                        System.out.println("Enter serie name: ");
                        String name = scanner.nextLine();
                        series.setTitle(name);

                        System.out.println("Enter number of genres: ");
                        int noGenres = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noGenres; i++) {
                            System.out.println("Enter genre: ");
                            String userInput = scanner.nextLine().toUpperCase();
                            Genre genre = Genre.valueOf(userInput);
                            series.getGenres().add(genre);
                        }

                        System.out.println("Enter subject:  ");
                        series.setSubject(scanner.nextLine());

                        System.out.println("Enter number of regisors: ");
                        int noRegisors = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noRegisors; i++) {
                            System.out.println("Enter regisor: ");
                            String regisor = scanner.nextLine();
                            series.getRegisors().add(regisor);
                        }

                        System.out.println("Enter number of actors: ");
                        int noActors = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noActors; i++) {
                            System.out.println("Enter actor: ");
                            String actor = scanner.nextLine();
                            series.getActors().add(actor);
                        }

                        System.out.println("Enter number of seasons: ");
                        int noSeasons = scanner.nextInt();
                        scanner.nextLine();
                        for (int i = 0; i < noSeasons; i++) {
                            System.out.println("Enter season name: ");
                            String seasonName = scanner.nextLine();
                            System.out.println("Enter number of episodes: ");
                            int noEpisodes = scanner.nextInt();
                            scanner.nextLine();
                            List<Episode> episodes = new ArrayList<>();
                            for (int j = 0; j < noEpisodes; j++) {
                                System.out.println("Enter episode name: ");
                                String episodeName = scanner.nextLine();
                                System.out.println("Enter episode duration: ");
                                String episodeDuration = scanner.nextLine();
                                Episode episode = new Episode(episodeName, episodeDuration);
                                episodes.add(episode);
                            }
                            series.getSeries().put(seasonName, episodes);
                        }
                        System.out.println("Is this information correct? (y/n):");
                        System.out.println("Name: " + series.getTitle() + "\n"
                                + "Year: " + series.getYear() + "\n"
                                + "Seasons number: " + series.getSeasonsNr() + "\n"
                                + "Regisors: " + series.getRegisors() + "\n"
                                + "Actors: " + series.getActors() + "\n"
                                + "Genres: " + series.getGenres() + "\n"
                                + "Series: " + series.getSeries() + "\n");
                        switch (scanner.nextLine().charAt(0)) {
                            case 'y':
                                ((Staff) user).addProductionSystem(series);
                                instance.getSeries().add(series);
                                System.out.println("Serie " + series.getTitle() + "added successfully!");
                                continueEditingProduction = false;
                                break;
                            case 'n':
                                break;
                            default:
                                System.out.println("Invalid option!");
                                break;
                        }
                        ((Staff) user).addProductionSystem(series);
                        this.series.add(series);
                    } catch (Exception e) {
                        System.out.println("Invalid input!" + e.toString() + "\n");
                        scanner.nextLine();
                    }
                    break;
                case 3:
                    continueEditingProduction = false;
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void addActorIMDB(User user) {
        Actor actor = new Actor();
        Scanner scanner = new Scanner(System.in);
        boolean continueEditingActor = true;
        while(continueEditingActor) {
            try {
                System.out.println("Enter actor name: ");
                actor.setName(scanner.nextLine());
                System.out.println("Enter actor biography: ");
                actor.setBiography(scanner.nextLine());
                System.out.println("Enter number of productions: ");
                int noProductions = scanner.nextInt();
                scanner.nextLine();
                for (int i = 0; i < noProductions; i++) {
                    System.out.println("Enter production name: ");
                    String prod = scanner.nextLine();
                    System.out.println("Enter production type: ");
                    String type = scanner.nextLine().toUpperCase();
                    actor.getProductions().put(prod, ProductionType.valueOf(type));
                }
                System.out.println("Is this information correct? (y/n):");
                System.out.println("Name: " + actor.getName() + "\n"
                        + "Biography: " + actor.getBiography() + "\n"
                        + "Productions: " + actor.getProductions().toString() + "\n");
                switch (scanner.nextLine().charAt(0)) {
                    case 'y':
                        ((Staff) user).addActorSystem(actor);
                        continueEditingActor = false;
                        break;
                    case 'n':
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
                System.out.println("Actor " + actor.getName() + "added successfully!");
            } catch (Exception e) {
                System.out.println("Invalid input! Press any key to continue...");
                scanner.nextLine();
            }
        }
    }
    public void handleRegularFlow(Regular regular){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome back " + regular.getInformation().getCredentials().getEmail() + "!");
        boolean continueEditing = true;
        while(continueEditing) {
            System.out.println("Choose action: ");
            System.out.println("  1. View actors");
            System.out.println("  2. View productions");
            System.out.println("  3. View favourites");
            System.out.println("  4. Create request");
            System.out.println("  5. View notifications");
            System.out.println("  6. Exit");
            int i;
            try {
                int ch = scanner.nextInt();
                scanner.nextLine();
                switch (ch) {
                    case 1:
                        i = 0;
                        for (Actor a : actors) {
                            i++;
                            System.out.print(String.valueOf(i) + ". ");
                            a.displayInfo();
                        }
                        System.out.println("Choose action: ");
                        System.out.println("  1. Add to favourites");
                        System.out.println("  2. Go back");
                        int choice2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice2) {
                            case 1:
                                System.out.println("Enter actor name: ");
                                String name = scanner.nextLine();
                                System.out.println(name);
                                for (Actor a : actors) {
                                    if (a.getName().compareTo(name) == 0) {
                                        regular.addFavourite(a);
                                        break;
                                    }
                                }
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Invalid option!");
                                break;
                        }
                        break;
                    case 2:
                        System.out.println("Choose action: ");
                        System.out.println("  1. View movies");
                        System.out.println("  2. View series");
                        System.out.println("  3. View by genre");
                        try {
                            int c = scanner.nextInt();
                            scanner.nextLine();
                            switch (c) {
                                case 1:
                                    i = 0;
                                    for (Movies m : movies) {
                                        i++;
                                        System.out.print(String.valueOf(i) + ". ");
                                        m.displayInfo();
                                    }
                                    System.out.println("Choose action: ");
                                    System.out.println("  1. Add to favourites");
                                    System.out.println("  2. Add rating");
                                    System.out.println("  3. Go back");
                                    int choicee = scanner.nextInt();
                                    scanner.nextLine();
                                    switch (choicee) {
                                        case 1:
                                            System.out.println("Enter production name: ");
                                            String name = scanner.nextLine();
                                            boolean found = false;
                                            for (Movies m : movies) {
                                                i++;
                                                if (m.getTitle().compareTo(name) == 0) {
                                                    found = true;
                                                    regular.addFavourite(m);
                                                    System.out.println(m.getTitle() + " added to favourites!");
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                System.out.println("Movie not found!");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Enter production name: ");
                                            String name2 = scanner.nextLine();
                                            boolean found2 = false;
                                            for (Movies m : movies) {
                                                if (m.getTitle().compareTo(name2) == 0) {
                                                    found2 = true;
                                                    System.out.println("Enter rating: ");
                                                    int rating = scanner.nextInt();
                                                    scanner.nextLine();
                                                    System.out.println("Enter review: ");
                                                    String review = scanner.nextLine();
                                                    Rating r = new Rating();
                                                    r.setRating(rating);
                                                    r.setComments(review);
                                                    r.setUserName(regular.getInformation().getUserName());
                                                    regular.addRating(r, m.getTitle());
                                                    System.out.println("Rating added successfully!");
                                                    break;
                                                }
                                            }
                                            if (!found2) {
                                                System.out.println("Movie not found!");
                                            }
                                            break;
                                        case 3:
                                            break;
                                        default:
                                            System.out.println("Invalid option!");
                                            break;
                                    }
                                    break;
                                case 2:
                                    i = 0;
                                    for (Series s : series) {
                                        i++;
                                        System.out.print(String.valueOf(i) + ". ");
                                        s.displayInfo();
                                    }
                                    System.out.println("Choose action: ");
                                    System.out.println("  1. Add to favourites");
                                    System.out.println("  2. Add rating");
                                    System.out.println("  3. Go back");
                                    int choicee2 = scanner.nextInt();
                                    scanner.nextLine();
                                    switch (choicee2) {
                                        case 1:
                                            System.out.println("Enter production name: ");
                                            String name = scanner.nextLine();
                                            boolean found = false;
                                            for (Series s : series) {
                                                if (s.getTitle().compareTo(name) == 0) {
                                                    found = true;
                                                    regular.addFavourite(s);
                                                    System.out.println(s.getTitle() + " added to favourites!");
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                System.out.println("Serie not found!");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Enter production name: ");
                                            String name2 = scanner.nextLine();
                                            boolean found2 = false;
                                            for (Series s : series) {
                                                if (s.getTitle().compareTo(name2) == 0) {
                                                    found2 = true;
                                                    System.out.println("Enter rating: ");
                                                    int rating = scanner.nextInt();
                                                    scanner.nextLine();
                                                    System.out.println("Enter review: ");
                                                    String review = scanner.nextLine();
                                                    Rating r = new Rating();
                                                    r.setRating(rating);
                                                    r.setComments(review);
                                                    r.setUserName(regular.getInformation().getUserName());
                                                    s.getRatings().add(r);
                                                    System.out.println("Rating added successfully!");
                                                    break;
                                                }
                                            }
                                            if (!found2) {
                                                System.out.println("Serie not found!");
                                            }
                                            break;
                                        case 3:
                                            break;
                                        default:
                                            System.out.println("Invalid option!");
                                            break;
                                    }
                                    break;
                                case 3:
                                    System.out.println("Choose genre: ");
                                    System.out.println("  1. Action");
                                    System.out.println("  2. Adventure");
                                    System.out.println("  3. Comedy");
                                    System.out.println("  4. Crime");
                                    System.out.println("  5. Drama");
                                    System.out.println("  6. Fantasy");
                                    System.out.println("  7. Biography;");
                                    System.out.println("  8. Horror");
                                    System.out.println("  9. Mystery");
                                    System.out.println("  10. Romance");
                                    System.out.println("  11. SF");
                                    System.out.println("  12. Thriller");
                                    System.out.println("  13. Western");
                                    int genreChoice = scanner.nextInt();
                                    scanner.nextLine();
                                    Genre genre = null;
                                    switch (genreChoice) {
                                        case 1:
                                            genre = Genre.ACTION;
                                            break;
                                        case 2:
                                            genre = Genre.ADVENTURE;
                                            break;
                                        case 3:
                                            genre = Genre.COMEDY;
                                            break;
                                        case 4:
                                            genre = Genre.CRIME;
                                            break;
                                        case 5:
                                            genre = Genre.DRAMA;
                                            break;
                                        case 6:
                                            genre = Genre.FANTASY;
                                        case 7:
                                            genre = Genre.BIOGRAPHY;
                                            break;
                                        case 8:
                                            genre = Genre.HORROR;
                                            break;
                                        case 9:
                                            genre = Genre.MYSTERY;
                                            break;
                                        case 10:
                                            genre = Genre.ROMANCE;
                                            break;
                                        case 11:
                                            genre = Genre.SF;
                                            break;
                                        case 12:
                                            genre = Genre.THRILLER;
                                            break;
                                        case 13:
                                            genre = Genre.BIOGRAPHY;
                                            break;
                                        default:
                                            System.out.println("Invalid option!");
                                            break;
                                    }
                                    if (genre != null) {
                                        i = 0;
                                        for (Movies m : movies) {
                                            i++;
                                            if (m.getGenres().contains(genre)) {
                                                System.out.println(String.valueOf(i) + ". ");
                                                m.displayInfo();
                                            }
                                        }
                                        for (Series s : series) {
                                            i++;
                                            if (s.getGenres().contains(genre)) {
                                                System.out.println(String.valueOf(i) + ". ");
                                                s.displayInfo();
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid option!");
                                    break;
                            }
                        } catch(Exception e){
                            System.out.println("Invalid input!");
                        }
                        break;
                    case 3:
                        i = 0;
                        for (T t : regular.getFavourites()) {
                            i++;
                            if (t instanceof Actor) {
                                System.out.print(String.valueOf(i) + ". ");
                                ((Actor) t).displayInfo();
                            } else if (t instanceof Production) {
                                System.out.print(String.valueOf(i) + ". ");
                                ((Production) t).displayInfo();
                            }
                        }
                        System.out.println("Choose action: ");
                        System.out.println("  1. Remove from favourites");
                        System.out.println("  2. Go back");
                        int choice3 = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice3) {
                            case 1:
                                System.out.println("Enter production name: ");
                                String name = scanner.nextLine();
                                boolean found = false;
                                for (T t : regular.getFavourites()) {
                                    if (t.getName().compareTo(name) == 0) {
                                        found = true;
                                        regular.removeFavourite(t);
                                        System.out.println(t.getName() + " removed from favourites!");
                                        break;
                                    }
                                }
                                if (!found) {
                                    System.out.println("Production not found!");
                                }
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Invalid option!");
                                break;
                        }

                        break;
                    case 4:
                        System.out.println("Choose request type: ");
                        System.out.println("  1. Movie issue");
                        System.out.println("  2. Actor issue");
                        System.out.println("  3. Delete account");
                        System.out.println("  4. Others");
                        int requestType = scanner.nextInt();
                        scanner.nextLine();
                        switch (requestType) {
                            case 1:
                                System.out.println("Enter movie name: ");
                                String movieName = scanner.nextLine();
                                System.out.println("Enter description: ");
                                String description = scanner.nextLine();
                                instance.getRequestsHolder().addRequest(RequestType.MOVIE_ISSUE, regular.getInformation().getUserName(), movieName, description);
                                System.out.println("Request added successfully!");
                                break;
                            case 2:
                                System.out.println("Enter actor name: ");
                                String actorName = scanner.nextLine();
                                System.out.println("Enter description: ");
                                String description1 = scanner.nextLine();
                                instance.getRequestsHolder().addRequest(RequestType.ACTOR_ISSUE, regular.getInformation().getUserName(), actorName, description1);
                                System.out.println("Request added successfully!");
                                break;
                            case 3:
                                System.out.println("Enter description: ");
                                String description2 = scanner.nextLine();
                                instance.getRequestsHolder().addRequest(RequestType.DELETE_ACCOUNT, regular.getInformation().getUserName(), "", description2);
                                System.out.println("Request added successfully!");
                                break;
                            case 4:
                                System.out.println("Enter description: ");
                                String description3 = scanner.nextLine();
                                instance.getRequestsHolder().addRequest(RequestType.OTHERS, regular.getInformation().getUserName(), "", description3);
                                System.out.println("Request added successfully!");
                                break;
                            default:
                                System.out.println("Invalid option!");
                                break;
                        }
                        break;
                    case 5:
                        for (String n : regular.getNotifications()) {
                            System.out.println(n);
                        }
                        break;
                    case 6:
                        continueEditing = false;
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            }catch (Exception e){
                scanner.nextLine(); // Clear the invalid input before retrying
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }
    }
    public void handleAdminFlow(Admin admin){
        System.out.println("Welcome back " + admin.getInformation().getCredentials().getEmail() + "!");
        boolean continueEditing = true;
        Scanner scanner = new Scanner(System.in);
        while(continueEditing) {
            System.out.println("Choose action: ");
            System.out.println("  1. Add production");
            System.out.println("  2. Add actor");
            System.out.println("  3. Remove production");
            System.out.println("  4. Remove actor");
            System.out.println("  5. Update production");
            System.out.println("  6. Update actor");
            System.out.println("  7. Solve request:");
            System.out.println("  8. View actors");
            System.out.println("  9. View productions");
            System.out.println("  10. Add user");
            System.out.println("  11. Remove user");
            System.out.println("  12. View notifications");
            System.out.println("  13. Log out");
            try {
                int ch = scanner.nextInt();
                switch (ch) {
                    case 1:
                        instance.addProductionIMDB(admin);
                        break;
                    case 2:
                        instance.addActorIMDB(admin);
                        break;
                    case 3:
                        System.out.println("Enter production name: ");
                        admin.removeProductionSystem(scanner.nextLine());
                        break;
                    case 4:
                        System.out.println("Enter actor name: ");
                        admin.removeActorSystem(scanner.nextLine());
                        break;
                    case 5:
                        instance.updateProductionMethod(admin);
                        break;
                    case 6:
                        instance.updateActorMethod(admin);
                        break;
                    case 7:
                        instance.requestSolver(admin);
                        break;
                    case 8:
                        for (Actor a : actors) {
                            a.displayInfo();
                        }
                        break;
                    case 9:
                        for (Movies m : movies) {
                            m.displayInfo();
                        }
                        for (Series s : series) {
                            s.displayInfo();
                        }
                        break;
                    case 10:
                        System.out.println("Choose user type: ");
                        System.out.println("  1. Regular");
                        System.out.println("  2. Contributor");
                        System.out.println("  3. Admin");
                        switch (scanner.nextInt()) {
                            case 1:
                                admin.addUser(AccountType.REGULAR);
                                break;
                            case 2:
                                admin.addUser(AccountType.CONTRIBUTOR);
                                break;
                            case 3:
                                admin.addUser(AccountType.ADMIN);
                                break;
                            default:
                                System.out.println("Invalid option!");
                                break;
                        }
                        break;
                    case 11:
                        System.out.println("Enter userName: ");
                        String userName = scanner.nextLine();
                        int i = 0;
                        boolean foundUser = false;
                        for (User user : this.getUsers()) {
                            if (user.getUsername().compareTo(userName) == 0 && user instanceof Staff) {
                                for (T t : ((Staff)user).getProductionsAndActors()){
                                    for (User u: this.getUsers()){
                                        if(u instanceof Admin){
                                            ((Admin) u).getProductionsAndActors().add(t);
                                        }
                                    }
                                }
                                this.getUsers().remove(i);
                                foundUser = true;
                                System.out.println("User removed succesfully!");
                                break;
                            }
                        }
                        if (!foundUser) {
                            System.out.println("User not found!");
                        }
                        break;
                    case 12:
                        for (String n : admin.getNotifications()) {
                            System.out.println(n);
                        }
                        break;
                    case 13:
                        continueEditing = false;
                        break;

                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            } catch (Exception e){}
        }
    }

    public void handleContributorFlow(Contributor contributor){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome back " + contributor.getInformation().getCredentials().getEmail() + "!");
        boolean continueEditing = true;
        while(continueEditing) {
            System.out.println("Choose action: ");
            System.out.println("  1. Add production");
            System.out.println("  2. Add actor");
            System.out.println("  3. Remove production");
            System.out.println("  4. Remove actor");
            System.out.println("  5. Update production");
            System.out.println("  6. Update actor");
            System.out.println("  7. Solve request:");
            System.out.println("  8. View actors");
            System.out.println("  9. View productions");
            System.out.println("  10. View notifications");
            System.out.println("  11. Exit");
                switch (scanner.nextInt()) {
                    case 1:
                        instance.addProductionIMDB(contributor);
                        break;
                    case 2:
                        instance.addActorIMDB(contributor);
                        break;
                    case 3:
                        System.out.println("Enter production name: ");
                        contributor.removeProductionSystem(scanner.nextLine());
                        break;
                    case 4:
                        System.out.println("Enter actor name: ");
                        contributor.removeActorSystem(scanner.nextLine());
                        break;
                    case 5:
                        instance.updateProductionMethod(contributor);
                        break;
                    case 6:
                        instance.updateActorMethod(contributor);
                        break;
                    case 7:
                        instance.requestSolver(contributor);
                        break;
                    case 8:
                        for(Actor a: actors){
                            a.displayInfo();
                        }
                        break;
                    case 9:
                        for(Movies m: movies){
                            m.displayInfo();
                        }
                        for(Series s: series){
                            s.displayInfo();
                        }
                        break;
                    case 10:
                        for(String n: contributor.getNotifications()){
                            System.out.println(n);
                        }
                        break;
                    case 11:
                        continueEditing = false;
                        break;
                    default:
                        System.out.println("Invalid option!");
                        break;
            }
        }
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to IMDB! Enter your credentials: ");
        boolean running = true;
        while(running) {
            System.out.println("Email: ");
            String username = scanner.nextLine();
            System.out.println("Password: ");
            String password = scanner.nextLine();
            User user = validateUser(username, password);
            if(user == null){
                System.out.println("Invalid credentials! Do you want to try again? (y/n)");
                String answer = scanner.nextLine();
                switch (answer){
                    case "y":
                        continue;
                    case "n":
                        System.out.println("Goodbye!");
                        running = false;
                        return;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }

            }
            else{
                if(user instanceof Admin){
                    handleAdminFlow((Admin) user);
                }
                else if(user instanceof Regular){
                    handleRegularFlow((Regular) user);
                }
                else if(user instanceof Contributor){
                    handleContributorFlow((Contributor) user);
                }
                else{
                    System.out.println("Invalid user type!");
                }
            }
        }
    }

    public void loadActors() {
        try {
            // Replace the file path with the actual path to your actors.json file
            String content = new String(Files.readAllBytes(Paths.get("/home/oda/ANUL2/IMDB-app/src/actors.json")));
            JSONArray actorsArray = new JSONArray(content);

            for (int i = 0; i < actorsArray.length(); i++) {
                JSONObject actorJson = actorsArray.getJSONObject(i);
                Actor actor = new Actor();
                // Assume Actor class has a constructor that accepts a JSONObject or setters for each field
                actor.setName(actorJson.optString("name"));
                JSONArray productionsArray = actorJson.getJSONArray("performances");
                for (int j = 0; j < productionsArray.length(); j++) {
                    JSONObject productionJson = productionsArray.getJSONObject(j);
                    actor.getProductions().put(productionJson.optString("title"), ProductionType.valueOf(productionJson.optString("type").toUpperCase()));
                }
                actor.setBiography(actorJson.optString("biography"));
                // ... set other fields ...
                this.actors.add(actor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    public void loadProductions() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/home/oda/ANUL2/IMDB-app/src/production.json")));
            JSONArray productionsArray = new JSONArray(content);

            for (int i = 0; i < productionsArray.length(); i++) {
                JSONObject productionJson = productionsArray.getJSONObject(i);
                String type = productionJson.optString("type").toUpperCase();

                if ("MOVIE".equalsIgnoreCase(type)) {
                    Movies movie = new Movies();
                    // Set movie fields from productionJson
                    movie.setTitle(productionJson.optString("title"));
                    JSONArray actorsArray = productionJson.getJSONArray("actors");
                    for (int j = 0; j < actorsArray.length(); j++) {
                        movie.getActors().add(actorsArray.getString(j));
                    }

                    JSONArray regisorsArray = productionJson.getJSONArray("directors");
                    for (int j = 0; j < regisorsArray.length(); j++) {
                        movie.getRegisors().add(regisorsArray.getString(j));
                    }

                    JSONArray genresArray = productionJson.getJSONArray("genres");
                    for (int j = 0; j < genresArray.length(); j++) {
                        movie.getGenres().add(Genre.valueOf(genresArray.getString(j).toUpperCase()));
                    }

                    JSONArray ratingsArray = productionJson.getJSONArray("ratings");
                    for (int j = 0; j < ratingsArray.length(); j++) {
                        Rating rating = new Rating();
                        JSONObject ratingJson = ratingsArray.getJSONObject(j);
                        rating.setUserName(ratingJson.optString("username"));
                        if(ratingJson.optString("rating").compareTo("")==0){
                            rating.setRating(0);
                        }
                        else{
                            rating.setRating(Integer.parseInt(ratingJson.optString("rating")));
                        }
                        rating.setComments(ratingJson.optString("comment"));
                        movie.getRatings().add(rating);
                    }
                    movie.setSubject(productionJson.optString("plot"));
                    movie.setDuration(productionJson.optString("duration"));
                    movie.setYear(productionJson.optInt("releaseYear"));
                    this.movies.add(movie);
                } else if ("SERIES".equalsIgnoreCase(type)) {
                    Series seriesObj = new Series();
                    // Set series fields from productionJson
                    seriesObj.setTitle(productionJson.optString("title"));
                    JSONArray actorsArray = productionJson.getJSONArray("actors");
                    for (int j = 0; j < actorsArray.length(); j++) {
                        seriesObj.getActors().add(actorsArray.getString(j));
                    }

                    JSONArray regisorsArray = productionJson.getJSONArray("directors");
                    for (int j = 0; j < regisorsArray.length(); j++) {
                        seriesObj.getRegisors().add(regisorsArray.getString(j));
                    }

                    JSONArray genresArray = productionJson.getJSONArray("genres");
                    for (int j = 0; j < genresArray.length(); j++) {
                        seriesObj.getGenres().add(Genre.valueOf(genresArray.getString(j).toUpperCase()));
                    }

                    JSONArray ratingsArray = productionJson.getJSONArray("ratings");
                    for (int j = 0; j < ratingsArray.length(); j++) {
                        Rating rating = new Rating();
                        JSONObject ratingJson = ratingsArray.getJSONObject(j);
                        System.out.println(ratingJson.optString("username"));
                        rating.setUserName(ratingJson.optString("username"));
                        if (ratingJson.optString("rating").compareTo("") == 0) {
                            rating.setRating(0);
                        } else {
                            rating.setRating(Integer.parseInt(ratingJson.optString("rating")));
                        }
                        rating.setComments(ratingJson.optString("comment"));
                        seriesObj.getRatings().add(rating);
                    }

                    seriesObj.setDescription(productionJson.optString("plot"));
                    seriesObj.setYear(productionJson.optInt("releaseYear"));
                    if (productionJson.optString("numSeasons").compareTo("") != 0) {
                        seriesObj.setSeasonsNr(Integer.parseInt(productionJson.optString("numSeasons")));
                    }
                    JSONObject seasonsJson = productionJson.getJSONObject("seasons");
                    for (String seasonName : seasonsJson.keySet()) {
                        Map<String, List<Episode>> seasonsMap= new HashMap<>();
                        JSONArray episodes = seasonsJson.getJSONArray(seasonName);
                        List<Episode> episodesList = new ArrayList<>();
                        for (int k = 0; k < episodes.length(); k++) {
                            JSONObject episodeJson = episodes.getJSONObject(k);
                            Episode episode = new Episode(episodeJson.optString("episodeName"), episodeJson.optString("duration"));
                            episodesList.add(episode);
                        }
                        seriesObj.getSeries().put(seasonName, episodesList);
                    }
                    this.series.add(seriesObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void loadRequests() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/home/oda/ANUL2/IMDB-app/src/requests.json")));
            JSONArray requestsArray = new JSONArray(content);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            for (int i = 0; i < requestsArray.length(); i++) {
                JSONObject requestJson = requestsArray.getJSONObject(i);
                RequestType type = RequestType.valueOf(requestJson.getString("type"));
                LocalDateTime dateTime = LocalDateTime.parse(requestJson.getString("createdDate"), formatter);
                String userName = requestJson.optString("username");
                String description = requestJson.optString("description");
                String productionTitle = requestJson.optString("actorName", requestJson.optString("movieTitle", ""));
                requestsHolder.addRequest(type, userName, productionTitle, description);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void addProductionToContributors(String productionName, User user,
                                          List<Movies> movies, List<Series> series) {
        for (Movies m : movies) {
            if (m.getTitle().equalsIgnoreCase(productionName)) {
                ((Staff) user).getProductionsAndActors().add(m);
                return;
            }
        }
        for (Series s : series) {
            if (s.getTitle().equalsIgnoreCase(productionName)) {
                ((Staff) user).getProductionsAndActors().add(s);
                return;
            }
        }
    }

    private void addActorToContributors(String actorName, User user, List<Actor> actors) {
        for (Actor a : actors) {
            if (a.getName().equalsIgnoreCase(actorName)) {
                ((Staff) user).getProductionsAndActors().add(a);
                return;
            }
        }
    }

    private void addProductionToFavorites(String productionName, User user,
                                             List<Movies> movies, List<Series> series) {
        for (Movies m : movies) {
            if (m.getTitle().equalsIgnoreCase(productionName)) {
                ((Staff) user).getFavorites().add(m);
                return;
            }
        }
        for (Series s : series) {
            if (s.getTitle().equalsIgnoreCase(productionName)) {
                ((Staff) user).getFavorites().add(s);
                return;
            }
        }
    }

    private void addActorToFavorites(String actorName, User user, List<Actor> actors) {
        for (Actor a : actors) {
            if (a.getName().equalsIgnoreCase(actorName)) {
                ((Staff) user).getFavorites().add(a);
                return;
            }
        }
    }

    public void loadAccounts() {
        JSONArray usersArray;
        try {
            String content = new String(Files.readAllBytes(Paths.get("/home/oda/ANUL2/IMDB-app/src/accounts.json")));
            usersArray = new JSONArray(content);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        UserFactory userFactory = new UserFactory();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userJson = usersArray.getJSONObject(i);
            User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
            JSONObject informationJson = userJson.getJSONObject("information");
            JSONObject credentialsJson = informationJson.getJSONObject("credentials");
            informationBuilder.setGender(informationJson.optString("gender"))
                    .setCountry(informationJson.optString("country"))
                    .setBirthDate(LocalDate.from(LocalDate.parse(informationJson.optString("birthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .setAge(informationJson.optInt("age"))
                    .setName(informationJson.optString("name"))
                    .setUserName(userJson.optString("username"))
                    .setCredentials(new Credentials(credentialsJson.optString("email"), credentialsJson.optString("password")));
            User.Information information = informationBuilder.build();
            User user = userFactory.createUser(AccountType.valueOf(userJson.optString("userType").toUpperCase()), information);
            try {
                user.setExperience(userJson.optInt("experience"));
                if (userJson.has("notifications")) {
                    JSONArray notificationsArray = userJson.getJSONArray("notifications");
                    for (int j = 0; j < notificationsArray.length(); j++) {
                        user.getNotifications().add(notificationsArray.getString(j));
                    }
                }
                System.out.println(user.getNotifications().toString());
            }catch (Exception e) {}
            // Adding productions to favorites
            try {
                JSONArray productionsArray, actorsArray;
                productionsArray = userJson.getJSONArray("favoriteProductions");
                for (int j = 0; j < productionsArray.length(); j++) {
                    String production = productionsArray.getString(j);
                    addProductionToFavorites(production, user, movies, series);
                }
            } catch (Exception e) {
            }

            // Ading actors to favorites
            try {
                JSONArray actorsArray = userJson.getJSONArray("favoriteActors");
                for (int j = 0; j < actorsArray.length(); j++) {
                    String actor = actorsArray.getString(j);
                    addActorToFavorites(actor, user, actors);
                }
            } catch (Exception e) {
            }

            // Adding
            if (userJson.optString("userType").equalsIgnoreCase("ADMIN") ||
                    userJson.optString("userType").equalsIgnoreCase("CONTRIBUTOR")) {
                try {
                    JSONArray productionsArray = userJson.getJSONArray("productionsContribution");
                    for (int j = 0; j < productionsArray.length(); j++) {
                        String production = productionsArray.getString(j);
                        addProductionToContributors(production, user, movies, series);
                    }
                } catch (Exception e) {
                }

                try {
                    JSONArray actorsArray = userJson.getJSONArray("actorsContribution");
                    for (int j = 0; j < actorsArray.length(); j++) {
                        String actor = actorsArray.getString(j);
                        addActorToContributors(actor, user, actors);
                    }
                } catch (Exception e) {
                }
            }
            this.getUsers().add(user);
        }

    }
    public static void main (String[] args){
        IMDB imdb = IMDB.getInstance();
        if(imdb == null){
            System.out.println("Instance null exception!");
            return;
        }
        instance.loadActors();
        instance.loadProductions();
        instance.loadAccounts();
        instance.loadRequests();
//        for (User u: instance.getUsers()){
//            if (u instanceof Staff){
//                System.out.println("Name : " + u.getInformation().getBirthDate());
//                for (T t : ((Staff)u).getProductionsAndActors())
//                    System.out.println(t.getName());
//            }
//        }
        imdb.run();
    }
}