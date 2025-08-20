package model;


import java.io.Serializable;
import java.time.LocalDateTime;


public class Transaction implements Serializable {
private final TransactionType type;
private final String symbol;
private final int quantity;
private final double price;
private final LocalDateTime time;


public Transaction(TransactionType type, String symbol, int quantity, double price) {
this.type = type;
this.symbol = symbol.toUpperCase();
this.quantity = quantity;
this.price = price;
this.time = LocalDateTime.now();
}


public TransactionType getType() { return type; }
public String getSymbol() { return symbol; }
public int getQuantity() { return quantity; }
public double getPrice() { return price; }
public LocalDateTime getTime() { return time; }


@Override public String toString() {
return time + " | " + type + " " + quantity + " " + symbol + " @ " + String.format("%.2f", price);
}
}