package model;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PerformanceTracker implements Serializable {
private final List<PortfolioSnapshot> snapshots = new ArrayList<>();


public void record(LocalDateTime t, double equityValue, double cash) {
snapshots.add(new PortfolioSnapshot(t, equityValue, cash));
}


public List<PortfolioSnapshot> getSnapshots() { return snapshots; }


public double totalReturn() {
if (snapshots.isEmpty()) return 0.0;
double start = snapshots.get(0).equityValue;
double end = snapshots.get(snapshots.size() - 1).equityValue;
return (end - start) / start * 100.0;
}
}