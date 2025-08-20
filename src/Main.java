import market.Market;
import market.Stock;
import model.Portfolio;
import user.User;

import java.nio.file.*;
import java.util.*;

public class Main {
    private static final String SAVE_DIR = "data";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // --- Setup market and user ---
        Market market = new Market();
        seedMarket(market);

        User user = new User("Trader", 100_000.00); // starting cash ₹100,000
        Portfolio pf = user.getPortfolio();
        pf.snapshot(market); // initial snapshot

        // --- CLI loop ---
        boolean running = true;
        while (running) {
            System.out.println("\n=== STOCK TRADING SIM ===");
            System.out.println("1) Tick Market (update prices)");
            System.out.println("2) Show Quotes");
            System.out.println("3) Buy");
            System.out.println("4) Sell");
            System.out.println("5) View Portfolio");
            System.out.println("6) View Transactions");
            System.out.println("7) View Performance");
            System.out.println("8) Save Portfolio");
            System.out.println("9) Load Portfolio");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> { market.tick(); pf.snapshot(market); System.out.println("Market ticked."); }
                    case "2" -> System.out.println(market.quoteTable());
                    case "3" -> doBuy(market, pf);
                    case "4" -> doSell(market, pf);
                    case "5" -> showPortfolio(market, pf);
                    case "6" -> pf.getTransactions().forEach(System.out::println);
                    case "7" -> showPerformance(pf);
                    case "8" -> save(pf);
                    case "9" -> { pf = load(); if (pf != null) { user.setPortfolio(pf); System.out.println("Loaded portfolio."); } }
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private static void seedMarket(Market market) {
        market.addStock(new Stock("TCS", "Tata Consultancy", 3850));
        market.addStock(new Stock("INFY", "Infosys", 1500));
        market.addStock(new Stock("RELI", "Reliance", 2950));
        market.addStock(new Stock("HDFCB", "HDFC Bank", 1650));
        market.addStock(new Stock("WIPRO", "Wipro", 460));
    }

    private static void doBuy(Market market, Portfolio pf) {
        System.out.print("Symbol: ");
        String sym = sc.nextLine();
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        pf.buy(market, sym, qty);
        System.out.println("Bought " + qty + " " + sym + ".");
    }

    private static void doSell(Market market, Portfolio pf) {
        System.out.print("Symbol: ");
        String sym = sc.nextLine();
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        pf.sell(market, sym, qty);
        System.out.println("Sold " + qty + " " + sym + ".");
    }

    private static void showPortfolio(Market market, Portfolio pf) {
        System.out.println("Cash: ₹" + String.format("%.2f", pf.getCash()));
        System.out.println("Holdings:");
        if (pf.getHoldings().isEmpty()) {
            System.out.println("  (none)");
        } else {
            System.out.printf("%-8s %8s %10s %10s %10s\n", "SYMBOL", "QTY", "AVG_COST", "LAST", "MTM");
            System.out.println("-".repeat(50));
            pf.getHoldings().values().forEach(pos -> {
                double last = market.get(pos.getSymbol()).getPrice();
                double mtm = (last - pos.getAvgCost()) * pos.getQuantity();
                System.out.printf("%-8s %8d %10.2f %10.2f %10.2f\n",
                        pos.getSymbol(), pos.getQuantity(), pos.getAvgCost(), last, mtm);
            });
        }
        double pv = pf.positionsValue(market);
        System.out.println("Positions Value: ₹" + String.format("%.2f", pv));
        System.out.println("Total Equity:    ₹" + String.format("%.2f", pf.totalEquity(market)));
    }

    private static void showPerformance(Portfolio pf) {
        var snaps = pf.getPerformance().getSnapshots();
        if (snaps.isEmpty()) { System.out.println("No snapshots yet."); return; }
        double totalRet = pf.getPerformance().totalReturn();
        System.out.println("Snapshots: " + snaps.size());
        System.out.println("Total Return: " + String.format("%.2f", totalRet) + "%");
    }

    private static void save(Portfolio pf) throws Exception {
        Files.createDirectories(Path.of(SAVE_DIR));
        String path = SAVE_DIR + "/portfolio.bin";
        pf.saveTo(path);
        System.out.println("Saved to " + path);
    }

    private static Portfolio load() throws Exception {
        String path = SAVE_DIR + "/portfolio.bin";
        if (!Files.exists(Path.of(path))) {
            System.out.println("No save file at " + path);
            return null;
        }
        return Portfolio.loadFrom(path);
    }
}
