package subsystemA;
public class SeatReservation {
    public String id;
    public String seat;
    public double price;
    public ReservationStatus status;

    public SeatReservation(String id, String seat, double price, ReservationStatus status) {
        this.id = id; this.seat = seat; this.price = price; this.status = status;
    }
    public String toCsv() { return id + "," + seat + "," + price + "," + status; }
    public static SeatReservation fromCsv(String csv) {
        String[] parts = csv.split(",");
        return new SeatReservation(parts[0], parts[1], Double.parseDouble(parts[2]), ReservationStatus.valueOf(parts[3]));
    }
}