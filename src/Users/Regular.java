package Users;

import IMDB.IMDB;
import ProductionAndActors.Movies;
import ProductionAndActors.Production;
import ProductionAndActors.Rating;
import ProductionAndActors.Series.Series;
import ProductionAndActors.T;
import Requests.Request;
import Users.Notifications.Observer;

public class Regular extends User implements RequestManager, Observer {
    public Regular(Information information, AccountType accountType) {
        super(information, accountType);
    }


    @Override
    public void removeRequest(Request r) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        int i = 0;
        for (Request request : instance.getRequestsHolder().getRequests()){
            if (request.getName().compareTo(r.getName()) == 0){
                instance.getRequestsHolder().getRequests().remove(i);
                return;
            }
            i++;
        }
    }

    @Override
    public void createRequest(Request r) {
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        instance.getRequestsHolder().addRequest(r.getType(), r.getName(), r.getProductionTitle(), r.getDescription());
    }

    public User findUser(String name){
        IMDB instance = IMDB.getInstance();

        if(instance == null){
            System.out.println("Instance null exception!");
            return null;
        }
        for(User user : instance.getUsers()){
            if(user instanceof Staff){
                for (T t : ((Staff) user).getProductionsAndActors()){
                    if(t.getName().compareTo(name)==0)
                        return user;
                }
            }
        }
        return null;
    }
    public int addRating(Rating rating, String productionTitle){
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return 0;
        }
        for (Movies M : instance.getMovies()){
            if (M.getName().compareTo(productionTitle) == 0){
                M.getRatings().add(rating);
                User u = findUser(productionTitle);
                if(u!=null) {
                    u.notifyUser("A new rating has been added to your production!");
                }
                M.getObservers().add(this);
                return 1;
            }
        }

        for (Series S : instance.getSeries()){
            if (S.getName().compareTo(productionTitle) == 0){
                S.getRatings().add(rating);
                S.getObservers().add(this);
                User u = findUser(productionTitle);
                if(u!=null)
                    u.notifyUser("A new rating has been added to your production!");
                return 1;
            }
        }
        return 0;
    }
}
