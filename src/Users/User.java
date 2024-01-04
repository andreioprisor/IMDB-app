package Users;

import ProductionAndActors.T;

import java.time.LocalDateTime;
import java.util.*;

import Users.Notifications.Observer;

import java.util.List;
import java.util.SortedSet;
public abstract class User implements Observer {

    // Atributele clasei
    private Information information;
    public static class Information {
        // User information fields
        private Credentials credentials;
        private int id;
        private String name;
        private String country;
        private int age;
        private String gender; // F, M, N
        private LocalDateTime birthDate;
        private String userName;

        // Private constructor
        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
            this.id = builder.id;
            this.userName = builder.userName;
        }

        // Builder class for Information
        public static class InformationBuilder {
            private int id;
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDateTime birthDate;
            private String userName;

            public InformationBuilder setUserName(String userName) {
                this.userName = userName;
                return this;
            }

            public InformationBuilder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public InformationBuilder setName(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder setCountry(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder setAge(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder setGender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder setBirthDate(LocalDateTime birthDate) {
                this.birthDate = birthDate;
                return this;
            }
            public InformationBuilder setId(int id) {
                this.id = id;
                return this;
            }
            public Information build() {
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return this.credentials;
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public LocalDateTime getBirthDate() {
            return birthDate;
        }
        public int getId() {
            return id;
        }
        public String getUserName() {
            return userName;
        }
    }
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favourites = new TreeSet<>();

    public Information getInformation() {
        return this.information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public User(Information info, AccountType accountType) {
        this.accountType = accountType;
        this.information = info;
        notifications = new ArrayList<>();
    }
    public void addFavourite(T favourite){
        favourites.add(favourite);
    }

    public void removeFavourite(T favourite){
        favourites.remove(favourite);
    }

    public SortedSet<T> getFavorites() {
        return favourites;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public SortedSet<T> getFavourites() {
        return favourites;
    }

    public void setFavourites(SortedSet<T> favourites) {
        this.favourites = favourites;
    }
    @Override
    public void notifyUser(String message) {
        message = this.getInformation().getName() + ", you received a notification: " + "\n" + message;
        this.getNotifications().add(message);
    }
}
