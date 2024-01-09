package Requests;

import IMDB.IMDB;
import ProductionAndActors.Movies;
import ProductionAndActors.Production;
import ProductionAndActors.T;
import Users.Admin;
import Users.Notifications.Observer;
import Users.Notifications.Subject;
import Users.Staff;
import Users.StaffInterface;
import Users.User;

import java.util.ArrayList;
import java.util.List;

public class RequestsHolder  {
    private static List<Request> requests;
    private List<Observer> observers;
    public RequestsHolder(){
        requests = new ArrayList<>();
        observers = new ArrayList<>();
    }
    public List<Request> getRequests(){
        return requests;
    }
    public void setRequests(List<Request> requestss) {
        requests = requestss;
    }

    public void addRequest(RequestType type, String userName, String productionTitle, String description){
        IMDB instance = IMDB.getInstance();
        if(instance==null){
            System.out.println("IMDB is null");
            return;
        }
        Request r = new Request(type, userName, productionTitle, description);
        List<User> users = instance.getUsers();
        switch (type){
            case MOVIE_ISSUE, ACTOR_ISSUE:
                boolean found = false;
                for(User user : users)
                    if (user instanceof Staff ) {
                        for (T t : ((Staff) user).getProductionsAndActors()) {
                            if (t.getName().compareTo(productionTitle) == 0) {
                                found = true;
                                r.setSolverUsername(user.getInformation().getUserName());
                                System.out.println(user.getInformation().getUserName());
                                ((Staff) user).getRequests().add(r);
                                user.notifyUser("A new request has been assigned to you" + "( " + type + " " + t.getName() + ")");
                                break;
                            }
                        }
                    }
                if(!found){
                    System.out.println("Production not found!");
                }
                break;
            case DELETE_ACCOUNT:
                for(User user : users) {
                    if (user instanceof Admin) {
                        for (User u : ((Admin) user).getUsersAdded()) {
                            System.out.println(u.getInformation().getUserName() + "compared to " + userName + "\n");
                            if (u.getInformation().getUserName().compareTo(userName) == 0) {
                                r.setSolverUsername(user.getInformation().getUserName());
                                System.out.println(user.getInformation().getUserName());
                                user.notifyUser("A new request has been assigned to you: ( DELETE_ACCOUNT " + userName + ")");
                                break;
                            }
                        }
                        r.setSolverUsername(user.getInformation().getUserName());
                    }
                }
                break;
            case OTHERS:
                for (User user : users)
                    if (user instanceof StaffInterface) {
                        r.setSolverUsername(user.getInformation().getUserName());
                        System.out.println(user.getInformation().getUserName());
                        user.notifyUser("A new request has been assigned to you: ( OTHERS " + userName + ")");
                        break;
                    }
                r.setSolverUsername("Any staff member");
                break;
            default:
                System.out.println("Invalid request type");
                break;
        }
        requests.add(r);
    }

    public void removeRequest(Request r){
        int i = 0;
        for (Request request : requests) {
            if (request.getName().compareTo(r.getName()) == 0){
                requests.remove(i);
                return;
            }
        }
    }
}
