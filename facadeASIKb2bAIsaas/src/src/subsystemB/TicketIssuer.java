package subsystemB;
public class TicketIssuer {
    public ETicket issue(String reservationId) {
        return new ETicket("TKT-" + System.currentTimeMillis(), reservationId, TicketStatus.ISSUED);
    }
}