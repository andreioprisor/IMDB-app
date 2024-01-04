package ProductionAndActors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actor implements T{
    private String name;
    private Map<String, ProductionType> productions;
    private String biography;
    int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Actor() {
        productions = new HashMap<>();
    }
    @Override
    public int compareTo(T t){
        return this.name.compareTo(t.getName());
    }
    @Override
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ProductionType> getProductions() {
        return productions;
    }

    public void setProductions(Map<String, ProductionType> productions) {
        this.productions = productions;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void displayInfo(){
        System.out.println("Name: " + this.name);
        System.out.println("Productions: " + this.productions.toString());
        System.out.println("Biography: " + this.biography);
        System.out.println("\n");
    }
}
