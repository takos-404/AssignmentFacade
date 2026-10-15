package facade;
import subsystemA.ReservationRepository;
import subsystemB.TicketRepository;

public class AdminFacade {
    private ChainRepository chainRepo = new ChainRepository();
    private ReservationRepository repoA = new ReservationRepository();
    private TicketRepository repoB = new TicketRepository();

    public void printSystemState() {
        System.out.println("\n--- DATABASE STATE ---");
        System.out.println("⛓️ Chains:");
        chainRepo.findAll().forEach(c -> System.out.println("  " + c.id + " -> " + c.status));
        System.out.println("💺 Reservations (A):");
        repoA.findAll().forEach(r -> System.out.println("  " + r.id + " -> " + r.seat + " (" + r.status + ")"));
        System.out.println("🎟️ Tickets (B):");
        repoB.findAll().forEach(t -> System.out.println("  " + t.ticketId + " for " + t.reservationId + " (" + t.status + ")"));
        System.out.println("----------------------\n");
    }
}