package subsystemA;
public class FareCalculator {
    public double calculate(String seat) { return seat.startsWith("A") ? 500.0 : 200.0; }
}