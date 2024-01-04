package Requests;

import java.time.LocalDateTime;

public class Request {
    private RequestType type;
    private LocalDateTime dateTime;
    private String userName;
    private String productionTitle;
    private String solverUsername;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    private String description;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSolverUsername() {
        return solverUsername;
    }

    public void setSolverUsername(String solverUsername) {
        this.solverUsername = solverUsername;
    }

    public String getProductionTitle() {
        return productionTitle;
    }

    public void setProductionTitle(String productionTitle) {
        this.productionTitle = productionTitle;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }
    public Request(RequestType type, String userName, String productionTitle, String description){
        this.type = type;
        this.dateTime = LocalDateTime.now();
        this.userName = userName;
        this.productionTitle = productionTitle;
        this.description = description;
    }
}
