package Users;

import ProductionAndActors.Actor;
import ProductionAndActors.Movies;
import ProductionAndActors.Production;
import ProductionAndActors.Series.Series;
import ProductionAndActors.T;
import Requests.Request;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import IMDB.IMDB;
import Requests.RequestsHolder;
import Users.Notifications.Observer;
import Users.Strategy.ExperienceManager;
import Users.Strategy.ProdActorStrategy;
import Users.Strategy.RequestStrategy;

public class Staff extends User implements Observer {
    private List<Request> requests;
    private SortedSet<T> productionsAndActors;

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public SortedSet<T> getProductionsAndActors() {
        return productionsAndActors;
    }

    public void setProductionsAndActors(SortedSet<T> productionsAndActors) {
        this.productionsAndActors = productionsAndActors;
    }

    public Staff(Information information, AccountType accountType) {
        super(information, accountType);
        this.requests = new ArrayList<>();
        this.productionsAndActors = new TreeSet<>();
    }

    public void addActorSystem(Actor a) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        instance.getActors().add(a);
        productionsAndActors.add(a);
        if (this.getExperience()<Integer.MAX_VALUE){
            ExperienceManager experienceManager = new ExperienceManager(new RequestStrategy());
            experienceManager.updateExperience(this);
        }
    }

    public void removeActorSystem(String name) {
        IMDB instance = IMDB.getInstance();
        if (instance == null) {
            System.out.println("Instance null exception!");
            return;
        }
        int i = 0;
        for(Actor actor: instance.getActors()){
            if(name.compareTo(actor.getName())==0){
                instance.getActors().remove(i);
                return;
            }
            i++;
        }
        System.out.println("Actor not found!");
    }

    public void addProductionSystem(Production p) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        if(p instanceof Movies){
            Movies movie = (Movies)p;
            instance.getMovies().add(movie);
        } else if(p instanceof Series){
            Series serie = (Series)p;
            instance.getSeries().add(serie);
        } else {
            System.out.println("Production type not found!");
            return;
        }
        productionsAndActors.add(p);
        if (this.getExperience() < Integer.MAX_VALUE ) {
            ExperienceManager experienceManager = new ExperienceManager(new ProdActorStrategy());
            experienceManager.updateExperience(this);
        }
    }

    public void removeProductionSystem(String name) {
        IMDB instance = IMDB.getInstance();
        int i = 0;
        List<Movies> movies = instance.getMovies();
        for(Production movie: movies){
            if(name.compareTo(movie.getName())==0){
                instance.getMovies().remove(i);
                return;
            }
            i++;
        }
        i = 0;
        List<Series> series = instance.getSeries();
        for(Series serie: series){
            if(name.compareTo(serie.getName())==0){
                instance.getSeries().remove(i);
                return;
            }
            i++;
        }
        System.out.println("Production not found!");
    }

    public void updateActor(Actor a) {
        IMDB instance = IMDB.getInstance();
        List<Actor> actors = instance.getActors();

        // Use an iterator to safely remove the old actor while iterating
        Iterator<Actor> iterator = actors.iterator();
        while (iterator.hasNext()) {
            Actor actor = iterator.next();
            if (a.getName().equals(actor.getName())) {
                iterator.remove(); // Safely remove the old actor
                actors.add(a);     // Add the updated actor
                return;
            }
        }
    }

    public void updateProduction(Production p) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        if(p instanceof Movies){
            List<Movies> movies = instance.getMovies();
            Iterator<Movies> iterator = movies.iterator();
            while (iterator.hasNext()) {
                Movies movie = iterator.next();
                if (p.getName().equals(movie.getName())) {
                    iterator.remove(); // Safely remove the old movie
                    movies.add((Movies)p);     // Add the updated movie
                    return;
                }
            }
        }
        else if(p instanceof Series){
            List<Series> series = instance.getSeries();
            Iterator<Series> iterator = series.iterator();
            while (iterator.hasNext()) {
                Series serie = iterator.next();
                if (p.getName().equals(serie.getName())) {
                    iterator.remove(); // Safely remove the old serie
                    series.add((Series)p);     // Add the updated serie
                    return;
                }
            }
        }
        else {
            System.out.println("Production type not found!");
        }
    }

    public void solveRequest(Request r){
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        System.out.println("Those are your requests: \n");
        int i = 1;
        for (Request request : requests) {
            System.out.println(i + ". " + request.getName());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Date: " + request.getDateTime());
            System.out.println("ProductionTitle: " + request.getProductionTitle());
            System.out.println("");
            i++;
        }

        System.out.println("Choose action: ");
        System.out.println("  1. Solve request");
        System.out.println("  2. Reject request");
        Scanner scanner = new Scanner(System.in);
        RequestsHolder requestsHolder = instance.getRequestsHolder();
        switch (scanner.nextInt()){
            case 1:
                i = 0;
                for (User user : instance.getUsers()) {
                    if (user.getInformation().getName().compareTo(r.getName())==0) {
                        if (this.getExperience() < Integer.MAX_VALUE){
                            ExperienceManager experienceManager = new ExperienceManager(new RequestStrategy());
                            experienceManager.updateExperience(user);
                        }
                        user.notifyUser("Your request regarding: " + r.getDescription() + "has been solved!");
                        break;
                    }
                    i++;
                }
                if(i == instance.getUsers().size()){
                    System.out.println("User not found!");
                    return;
                }
                i = 0;
                for (Request request : requestsHolder.getRequests()) {
                    if (request.getName().compareTo(r.getName())==0) {
                        requestsHolder.getRequests().remove(i);
                        break;
                    }
                    i++;
                }
                System.out.println("Request solved!");
                break;
            case 2:
                System.out.println("Request rejected!");
                break;
            default:
                System.out.println("Invalid input!");
                break;
        }
    }

}
