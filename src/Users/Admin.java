package Users;

import IMDB.IMDB;
import Requests.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends Staff{
    private List<User> usersAdded;
    public Admin(Information information, AccountType accountType){
        super(information, accountType);
        usersAdded = new ArrayList<>();
        super.setExperience(Integer.MAX_VALUE);
    }
    public void addUser(AccountType accountType){
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        UserFactory userFactory = new UserFactory();
        boolean running = true;
        while(running){
            System.out.println("Enter email: ");
            String email = scanner.next();
            System.out.println("Enter password: ");
            String password = scanner.next();
            System.out.println("Enter name:");
            String name = scanner.next();
            System.out.println("Enter country:");
            String country = scanner.next();
            System.out.println("Enter age:");
            int age = scanner.nextInt();
            System.out.println("Enter gender: ");
            String gender = scanner.next();
            System.out.println("Enter birth date: ");
            String birthDate = scanner.next();
            System.out.println("Is this information correct? ");
            System.out.println("email: " + email + "\n"
                                + "Password: " + password + "\n"
                                + "Name: " + name + "\n"
                                + "Country: " + country + "\n"
                                + "Age: " + age + "\n"
                                + "Gender: " + gender + "\n"
                                + "Birth date: " + birthDate + "\n"
                                + "(y/n)");
            switch (scanner.next().charAt(0)){
                case 'y':
                    User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder();
                    informationBuilder.setCredentials(new Credentials(email, password))
                            .setAge(age)
                            .setBirthDate(LocalDateTime.parse(birthDate))
                            .setCountry(country)
                            .setGender(gender)
                            .setId(instance.getUsers().size()+1)
                            .setName(name)
                            .setUserName(name);
                    User user = userFactory.createUser(accountType, informationBuilder.build());
                    instance.getUsers().add(user);
                    running = false;
                    break;
                case 'n':
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }

    }

    public List<User> getUsersAdded() {
        return usersAdded;
    }

    public void setUsersAdded(List<User> usersAdded) {
        this.usersAdded = usersAdded;
    }

    public void removeUser(int id){
        IMDB instance = IMDB.getInstance();
        if(instance == null){
            System.out.println("Instance null exception!");
            return;
        }
        User user = instance.getUsers().get(id);
        int i = 0;
        for (Request r : instance.getRequestsHolder().getRequests()) {
            if (r.getName().compareTo(user.getInformation().getUserName())==0) {
                instance.getRequestsHolder().getRequests().remove(i);
                break;
            }
            i++;
        }
    }
}
