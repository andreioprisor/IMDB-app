package Users;

public class UserFactory {
    public User createUser(AccountType accountType, User.Information information) {
        switch (accountType) {
            case REGULAR:
                return new Regular(information, accountType);
            case CONTRIBUTOR:
                return new Contributor(information, accountType);
            case ADMIN:
                return new Admin(information, accountType);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }
}
