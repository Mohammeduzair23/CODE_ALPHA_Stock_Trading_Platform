package model;

import market.Market;
import market.Stock;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Portfolio implements Serializable {
    private double cash;
    private final Map<String, Position> holdings = new LinkedHashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final PerformanceTracker performance = new PerformanceTracker();

    public Portfolio(double startingCash) {
        this.cash = startingCash;
    }

    public double getCash() { return cash; }
    public Map<String, Position> getHoldings() { return holdings; }
    public List<Transaction> getTransactions() { return transactions; }
    public PerformanceTracker getPerformance() { return performance; }

    // === Buy operation ===
    public void buy(Market market, String symbol, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        Stock s = market.get(symbol);
        if (s == null) throw new IllegalArgumentException("Unknown symbol: " + symbol);

        double cost = s.getPrice() * qty;
        if (cost > cash + 1e-9) throw new IllegalArgumentException("Insufficient cash");

        cash -= cost;
        Position p = holdings.computeIfAbsent(s.getSymbol(), Position::new);
        p.buy(qty, s.getPrice());

        transactions.add(new Transaction(TransactionType.BUY, s.getSymbol(), qty, s.getPrice()));
    }

    // === Sell operation ===
    public void sell(Market market, String symbol, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        Position p = holdings.get(symbol.toUpperCase());
        if (p == null || p.getQuantity() < qty) throw new IllegalArgumentException("Not enough shares to sell");

        Stock s = market.get(symbol);
        if (s == null) throw new IllegalArgumentException("Unknown symbol: " + symbol);

        p.sell(qty);
        double proceeds = s.getPrice() * qty;
        cash += proceeds;

        transactions.add(new Transaction(TransactionType.SELL, s.getSymbol(), qty, s.getPrice()));

        if (p.getQuantity() == 0) holdings.remove(symbol.toUpperCase()); // cleanup
    }

    // === Portfolio values ===
    public double positionsValue(Market market) {
        double sum = 0.0;
        for (Position p : holdings.values()) {
            Stock s = market.get(p.getSymbol());
            if (s != null) sum += p.getQuantity() * s.getPrice();
        }
        return sum;
    }

    public double totalEquity(Market market) {
        return cash + positionsValue(market);
    }

    // === Record portfolio snapshot ===
    public void snapshot(Market market) {
        performance.record(LocalDateTime.now(), totalEquity(market), cash);
    }

    // === Persistence (Save/Load) ===
    public void saveTo(String filepath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(this);
        }
    }

    public static Portfolio loadFrom(String filepath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            return (Portfolio) ois.readObject();
        }
    }
}
