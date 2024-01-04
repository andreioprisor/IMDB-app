package Users.Strategy;

import Users.User;

public class ExperienceManager {
    private ExperienceStrategy strategy;

    public ExperienceManager(ExperienceStrategy strategy) {
        this.strategy = strategy;
    }

    public void updateExperience(User user) {
        int experience = strategy.calculateExperience();
        user.setExperience(user.getExperience() + experience);
    }
}
