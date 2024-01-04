package Users.Strategy;

public class RequestStrategy implements ExperienceStrategy{
    @Override
    public int calculateExperience() {
        return 30;
    }
}
