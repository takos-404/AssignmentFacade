package subsystemB;
public class ETicket {
    public String ticketId;
    public String reservationId;
    public TicketStatus status;

    public ETicket(String ticketId, String reservationId, TicketStatus status) {
        this.ticketId = ticketId; this.reservationId = reservationId; this.status = status;
    }
    public String toCsv() { return ticketId + "," + reservationId + "," + status; }
    public static ETicket fromCsv(String csv) {
        String[] parts = csv.split(",");
        return new ETicket(parts[0], parts[1], TicketStatus.valueOf(parts[2]));
    }
}