package market;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Stock implements Serializable {
private final String symbol;
private final String name;
private double price; // current price
private final List<Double> history = new ArrayList<>();


public Stock(String symbol, String name, double initialPrice) {
this.symbol = symbol.toUpperCase();
this.name = name;
this.price = initialPrice;
history.add(initialPrice);
}


public String getSymbol() { return symbol; }
public String getName() { return name; }
public double getPrice() { return price; }
public List<Double> getHistory() { return history; }


public void setPrice(double price) {
this.price = price;
history.add(price);
}
}