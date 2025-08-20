package model;


import java.io.Serializable;
import java.time.LocalDateTime;


public class PortfolioSnapshot implements Serializable {
public final LocalDateTime time;
public final double equityValue; // positions + cash
public final double cash;


public PortfolioSnapshot(LocalDateTime time, double equityValue, double cash) {
this.time = time;
this.equityValue = equityValue;
this.cash = cash;
}
}