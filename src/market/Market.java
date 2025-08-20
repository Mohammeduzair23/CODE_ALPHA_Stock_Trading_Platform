package market;


import java.io.Serializable;
import java.util.*;


public class Market implements Serializable {
private final Map<String, Stock> stocks = new LinkedHashMap<>();
private final Random rng = new Random();


public void addStock(Stock s) {
stocks.put(s.getSymbol(), s);
}


public Stock get(String symbol) {
return stocks.get(symbol.toUpperCase());
}


public Collection<Stock> list() { return stocks.values(); }


/**
* Random‑walk tick: small percentage move per stock per tick.
*/
public void tick() {
for (Stock s : stocks.values()) {
double p = s.getPrice();
double drift = 0.0005; // tiny upward drift
double vol = 0.02; // 2% daily‑ish step
double shock = (rng.nextGaussian() * vol) + drift;
double next = Math.max(0.01, p * (1.0 + shock));
s.setPrice(round2(next));
}
}


public String quoteTable() {
StringBuilder sb = new StringBuilder();
sb.append(String.format("%-8s %-18s %10s\n", "SYMBOL", "NAME", "PRICE"));
sb.append("-".repeat(40)).append('\n');
for (Stock s : stocks.values()) {
sb.append(String.format("%-8s %-18s %10.2f\n", s.getSymbol(), s.getName(), s.getPrice()));
}
return sb.toString();
}


private static double round2(double x) { return Math.round(x * 100.0) / 100.0; }
}