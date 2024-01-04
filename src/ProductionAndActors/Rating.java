package ProductionAndActors;

public class Rating {
    private int rating;
    private String userName;
    private String comments;

    public void setRating(int rating){
        this.rating = rating;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setComments(String comments){
        this.comments = comments;
    }
    public int getRating(){
        return this.rating;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getComments(){
        return this.comments;
    }
}
