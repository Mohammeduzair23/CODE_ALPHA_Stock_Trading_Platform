package user;

import model.Portfolio;
import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private Portfolio portfolio;

    public User(String username, double startingCash) {
        this.username = username;
        this.portfolio = new Portfolio(startingCash);
    }

    public String getUsername() { return username; }
    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio p) { this.portfolio = p; }
}
