package ProductionAndActors.Series;

import ProductionAndActors.Production;
import ProductionAndActors.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production {
    private int year;
    private int SeasonsNr;
    private Map<String, List<Episode>> series;
    public Series(){
        series = new HashMap<>();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSeasonsNr() {
        return SeasonsNr;
    }

    public void setSeasonsNr(int seasonsNr) {
        SeasonsNr = seasonsNr;
    }

    public Map<String, List<Episode>> getSeries() {
        return series;
    }

    public void setSeries(Map<String, List<Episode>> series) {
        this.series = series;
    }

    public void displayInfo(){
        System.out.println("Title: " + super.getName());
        System.out.println("Year: " + this.year);
        System.out.println("Seasons number: " + this.SeasonsNr);
        System.out.println("Regisors: " + super.getRegisors());
        System.out.println("Actors: " + super.getActors());
        System.out.println("Genres: " + super.getGenres());
        System.out.println("Ratings: (" + super.getRatings().size() + ")");
        int i = 0;
        System.out.println("");
        for (Rating rating : super.getRatings()) {
            i++;
            System.out.print(String.format("%d.", i));
            System.out.println(" Rating: " + rating.getRating());
            System.out.println("  User: " + rating.getUserName());
            System.out.println("  Comments: " + rating.getComments());
            System.out.println("");
        }
        System.out.println("\n");
        System.out.println("Subject: " + super.getSubject());
        System.out.println("Average rating: " + super.getAvgRating());
        System.out.println("\n");
        System.out.println("Seasons: ");
        int j =0;
        for (Map.Entry<String, List<Episode>> entry : series.entrySet()) {
            j++;
            System.out.println("  "  + entry.getKey() + ": ");
            i = 0;
            for (Episode episode : entry.getValue()) {
                i++;
                System.out.println("    Title: \""  + episode.getTitle()+ "\"");
                System.out.println("    Duration: " + episode.getDuration());
                System.out.println("");
            }
            System.out.println("");
        }

    }
}
