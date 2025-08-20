package model;


import java.io.Serializable;


public class Position implements Serializable {
private final String symbol;
private int quantity;
private double avgCost; // volume‑weighted average cost


public Position(String symbol) {
this.symbol = symbol.toUpperCase();
}


public String getSymbol() { return symbol; }
public int getQuantity() { return quantity; }
public double getAvgCost() { return avgCost; }


public void buy(int qty, double price) {
double totalCost = avgCost * quantity + price * qty;
quantity += qty;
avgCost = totalCost / quantity;
}


public void sell(int qty) {
if (qty > quantity) throw new IllegalArgumentException("Sell qty exceeds position");
quantity -= qty;
if (quantity == 0) avgCost = 0.0; // reset when flat
}
}