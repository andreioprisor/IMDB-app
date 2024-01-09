package Users;

import ProductionAndActors.Actor;
import ProductionAndActors.Movies;
import ProductionAndActors.Production;
import ProductionAndActors.Series.Series;
import ProductionAndActors.T;
import Requests.Request;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.*;

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

    public int addActorSystem(Actor a) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return 0;
        }
        if (instance.getActors().contains(a)) {
            return 0;
        }
        instance.getActors().add(a);
        productionsAndActors.add(a);
        if (this.getExperience()<Integer.MAX_VALUE){
            ExperienceManager experienceManager = new ExperienceManager(new RequestStrategy());
            experienceManager.updateExperience(this);
        }
        return 1;
    }

    public boolean removeActorSystem(String name) {
        IMDB instance = IMDB.getInstance();
        if (instance == null) {
            System.out.println("Instance null exception!");
            return false;
        }
        boolean found = false;
        for (T t : productionsAndActors) {
            if (t.getName().equals(name)) {
                found = true;
                productionsAndActors.remove(t);
                break;
            }
        }
        if(!found&&this instanceof Contributor){
            System.out.println("Actor not found!");
            return false;
        }

        int i = 0;
        for(Actor actor: instance.getActors()){
            if(name.compareTo(actor.getName())==0){
                instance.getActors().remove(i);
                return true;
            }
            i++;
        }
        System.out.println("Actor not found!");
        return false;
    }

    public int addProductionSystem(Production p) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return 0;
        }
        if(p instanceof Movies){
            Movies movie = (Movies)p;
            instance.getMovies().add(movie);
        } else if(p instanceof Series){
            Series serie = (Series)p;
            instance.getSeries().add(serie);
        } else {
            System.out.println("Production type not found!");
            return 0;
        }
        productionsAndActors.add(p);
        if (this.getExperience() < Integer.MAX_VALUE ) {
            ExperienceManager experienceManager = new ExperienceManager(new ProdActorStrategy());
            experienceManager.updateExperience(this);
        }
        return 1;
    }

    public boolean  removeProductionSystem(String name) {

        boolean found = false;
        for (T t : productionsAndActors) {
            if (t.getName().equals(name)) {
                found = true;
                productionsAndActors.remove(t);
                break;
            }
        }
        if(this instanceof Contributor && !found){
            System.out.println("Production not found!");
            return false;
        }

        IMDB instance = IMDB.getInstance();
        int i = 0;
        List<Movies> movies = instance.getMovies();
        for(Production movie: movies){
            if(name.compareTo(movie.getName())==0){
                instance.getMovies().remove(i);
                return true;
            }
            i++;
        }
        i = 0;
        List<Series> series = instance.getSeries();
        for(Series serie: series){
            if(name.compareTo(serie.getName())==0){
                instance.getSeries().remove(i);
                return false;
            }
            i++;
        }
        System.out.println("Production not found!");
        return false;
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
        System.out.println("  1. Solve a request");
        System.out.println("  2. Reject request");
        Scanner scanner = new Scanner(System.in);
        RequestsHolder requestsHolder = instance.getRequestsHolder();
        switch (scanner.nextInt()){
            case 1:
                boolean found = false;
                for (User user : instance.getUsers()) {
                    if (user.getInformation().getName().compareTo(r.getName())==0) {
                        if (super.getExperience() < Integer.MAX_VALUE){
                            ExperienceManager experienceManager = new ExperienceManager(new RequestStrategy());
                            experienceManager.updateExperience(user);
                        }
                        found = true;
                        user.notifyUser("Your request regarding: " + r.getDescription() + "has been solved!");
                        break;
                    }
                }
                if(!found){
                    System.out.println("User not found!");
                    return;
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
        scanner.nextLine();
        instance.getRequestsHolder().removeRequest(r);
        int i = 0;
        for (Request req : this.requests){
            if(req.getName().compareTo(r.getName())==0){
                this.requests.remove(i);
            }
            i++;
        }
    }
    public void solveRequestGUI(Request r) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        JDialog dialog = new JDialog();
        dialog.setTitle("Solve Request");
        dialog.setSize(300, 200);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        JLabel requestLabel = new JLabel("Request: " + r.getDescription());
        dialog.add(requestLabel);

        JButton solveButton = new JButton("Solve Request");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = false;
                for (User user : instance.getUsers()) {
                    if (user.getInformation().getUserName().compareTo(r.getName())==0) {
                        if (Staff.super.getExperience() < Integer.MAX_VALUE){
                            ExperienceManager experienceManager = new ExperienceManager(new RequestStrategy());
                            experienceManager.updateExperience(user);
                        }
                        found = true;
                        System.out.println("User found!");
                        user.notifyUser("Your request regarding: " + r.getDescription() + "has been solved!");
                        break;
                    }
                }
                if(!found){
                    System.out.println("User not found!");
                    return;
                }
                JOptionPane.showMessageDialog(dialog, "Request solved!");
                dialog.dispose();
            }
        });
        dialog.add(solveButton);

        JButton rejectButton = new JButton("Reject Request");
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement rejecting logic here
                JOptionPane.showMessageDialog(dialog, "Request rejected!");
                dialog.dispose();
            }
        });
        dialog.add(rejectButton);
        dialog.setVisible(true);
        instance.getRequestsHolder().removeRequest(r);
        int i = 0;
        for (Request req : this.requests){
            if(req.getName().compareTo(r.getName())==0){
                this.requests.remove(i);
            }
            i++;
        }
    }
}
