package ProductionAndActors;

import Users.Notifications.Observer;
import Users.Notifications.Subject;
import Users.Staff;
import Users.User;

import java.util.ArrayList;
import java.util.List;
import IMDB.IMDB;


public abstract class Production implements T, Subject {
    private String title;
    private List <Observer> observers;
    private List<String> regisors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String subject;
    private double avgRating;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Production(){
        regisors = new ArrayList<>();
        actors = new ArrayList<>();
        genres = new ArrayList<>();
        ratings = new ArrayList<>();
        observers = new ArrayList<>();
    }
    public void updateAvgRating(){
        double sum = 0;
        for(Rating rating : this.ratings){
            sum += rating.getRating();
        }
        this.avgRating = sum / this.ratings.size();
    }
    public abstract void displayInfo();
    @Override
    public int compareTo(T t){
        return this.title.compareTo(t.getName());
    }
    @Override
    public String getName(){
        return this.title;
    }
    public List<String> getRegisors(){
        return this.regisors;
    }
    public List<String> getActors(){
        return this.actors;
    }
    public List<Genre> getGenres(){
        return this.genres;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRegisors(List<String> regisors) {
        this.regisors = regisors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public void addRating(Rating rating){
        this.ratings.add(rating);
        this.updateAvgRating();
        for (Observer observer : observers){
            observer.notifyUser(this.title + " has a new rating!");
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void setObservers(List<Observer> observers) {
        this.observers = observers;
    }

    public void addObserver(Observer observer){
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer){
        this.observers.remove(observer);
    }
    public void notifyObservers(String message){
        for (Observer observer : observers){
            observer.notifyUser(message);
        }
    }
}
