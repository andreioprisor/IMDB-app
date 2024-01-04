package ProductionAndActors;

public class Movies extends Production{
    private String duration;
    private int year;

    public String getDuration(){
        return this.duration;
    }
    public int getYear(){
        return this.year;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public void setYear(int year){
        this.year = year;
    }
    public void displayInfo(){
        System.out.println("Title: " + super.getName());
        System.out.println("Duration: " + this.getDuration());
        System.out.println("Year: " + this.getYear());
        System.out.println("Regisors: " + super.getRegisors());
        System.out.println("Actors: " + super.getActors());
        System.out.println("Genres: " + super.getGenres());
        System.out.println("Ratings: (" + super.getRatings().size() + ")");
        int i = 0;
        for (Rating rating : super.getRatings()) {
            i++;
            System.out.print(String.format("%d.", i));
            System.out.println(" Rating: " + rating.getRating());
            System.out.println("  User: " + rating.getUserName());
            System.out.println("  Comments: " + rating.getComments());
        }
        System.out.println("\n");
        System.out.println("Subject: " + super.getSubject());
        System.out.println("Average rating: " + super.getAvgRating());
        System.out.println("\n");
    }
}
